package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.etherscan.EtherscanService;
import cloud.cinder.ethereum.parity.domain.MethodSignature;
import cloud.cinder.ethereum.parity.MethodSignatureService;
import cloud.cinder.cindercloud.token.service.TokenService;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import cloud.cinder.ethereum.transaction.Transaction;
import cloud.cinder.ethereum.transaction.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import rx.Observable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private Web3jGateway web3jGateway;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BlockService blockService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EtherscanService etherscanService;
    @Autowired
    private MethodSignatureService methodSignatureService;

    @Transactional
    public Observable<Slice<Transaction>> findByAddress(final String address, final Pageable pageable) {
        final Slice<Transaction> result = transactionRepository.findByAddressFromOrTo(address, pageable);
        result.getContent().forEach(this::enrichWithSpecialAddresses);
        if (result.hasContent()) {
            etherscanService.importByAddress(address);
        }
        return Observable.just(result);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public Observable<Slice<Transaction>> getTransactionsForBlock(final String blockHash, final Pageable pageable) {
        final Slice<Transaction> result = transactionRepository.findAllByBlockHashOrderByBlockHeightDesc(blockHash, pageable);
        result.getContent().forEach(this::enrichWithSpecialAddresses);
        return Observable.just(result);
    }

    @Transactional
    public Observable<Transaction> getTransaction(final String transactionHash) {
        return transactionRepository.findOne(transactionHash)
                .map(Observable::just)
                .orElse(getInternalTransaction(transactionHash)
                        .map(this::enrichWithSpecialAddresses));
    }

    @Transactional(readOnly = true)
    public Optional<MethodSignature> getMethodSignature(final String transactionHash) {
        return getTransaction(transactionHash)
                .map(x -> {
                    if (x.hasInput()) {
                        return methodSignatureService.findSignature(x.getInput());
                    } else {
                        return Optional.<MethodSignature>empty();
                    }
                }).toBlocking().firstOrDefault(Optional.empty());
    }

    public Transaction enrichWithSpecialAddresses(final Transaction tx) {
        Optional<SpecialAddress> byAddress = addressService.findByAddress(tx.getFromAddress());
        if (byAddress.isPresent()) {
            tx.setSpecialFrom(byAddress.get());
        } else {
            tokenService.findByAddress(tx.getFromAddress()).ifPresent(x -> {
                tx.setSpecialFrom(
                        SpecialAddress.builder()
                                .address(tx.getFromAddress())
                                .name(x.getName())
                                .slug(x.getSlug())
                                .url(x.getWebsite())
                                .build()
                );
            });
        }
        addressService.findByAddress(tx.getFromAddress()).ifPresent(tx::setSpecialFrom);

        final String to = tx.isContractCreation() ? tx.getCreates() : tx.getToAddress();
        final Optional<SpecialAddress> specialToAddress = addressService.findByAddress(to);
        if (specialToAddress.isPresent()) {
            tx.setSpecialTo(specialToAddress.get());
        } else {
            tokenService.findByAddress(to).ifPresent(x -> {
                tx.setSpecialTo(
                        SpecialAddress.builder()
                                .address(to)
                                .name(x.getName())
                                .slug(x.getSlug())
                                .url(x.getWebsite())
                                .build()
                );
            });
        }
        return tx;
    }

    @Transactional(readOnly = true)
    public Slice<Transaction> getLastTransactions(Pageable pageable) {
        return transactionRepository.findAllOrOrderByBlockTimestampAsPage(pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getLast10Transactions() {
        return transactionRepository.findAllOrOrderByBlockTimestampAsList(new PageRequest(0, 10));
    }

    private Observable<Transaction> getInternalTransaction(final String transactionHash) {
        try {
            return web3jGateway.web3j().ethGetTransactionByHash(transactionHash)
                    .observable()
                    .filter(x -> x.getTransaction().isPresent())
                    .map(transaction -> transaction.getTransaction().get())
                    .map(tx -> {
                        final Block block = blockService.getBlock(tx.getBlockHash()).toBlocking().first();
                        if (block != null) {
                            return Transaction.builder()
                                    .blockHash(tx.getBlockHash())
                                    .blockHeight(block.getHeight())
                                    .fromAddress(tx.getFrom())
                                    .gas(tx.getGas())
                                    .hash(tx.getHash())
                                    .input(tx.getInput())
                                    .toAddress(tx.getTo())
                                    .blockTimestamp(Date.from(LocalDateTime.ofEpochSecond(block.getTimestamp().longValue(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                                    .value(tx.getValue())
                                    .gasPrice(tx.getGasPrice())
                                    .creates(tx.getCreates())
                                    .s(tx.getS())
                                    .r(tx.getR())
                                    .v(tx.getV())
                                    .status(getTransactionStatus(tx))
                                    .nonce(tx.getNonce())
                                    .transactionIndex(tx.getTransactionIndex())
                                    .build();
                        } else {
                            log.error("couldnt import {} because we dont have the block yet", tx.getHash());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(tx -> transactionRepository.save(tx));
        } catch (
                Exception ex)
        {
            return Observable.error(ex);
        }
    }


    private TransactionStatus getTransactionStatus(final org.web3j.protocol.core.methods.response.Transaction tx) {
        try {
            final EthGetTransactionReceipt send = web3jGateway.web3j().ethGetTransactionReceipt(tx.getHash()).send();

            if (send.getTransactionReceipt().isPresent()) {
                if (send.getTransactionReceipt().get().getStatus().equalsIgnoreCase("1") || send.getTransactionReceipt().get().getStatus().equalsIgnoreCase("0x1")) {
                    return TransactionStatus.SUCCESS;
                } else {
                    if (send.getTransactionReceipt().get().getGasUsed().equals(tx.getGas())) {
                        return TransactionStatus.THROWN;
                    } else {
                        return TransactionStatus.REVERTED;
                    }
                }
            } else {
                return TransactionStatus.UNKNOWN;
            }
        } catch (final Exception ex) {
            log.debug("Unable to fetch transaction receipt");
            return TransactionStatus.UNKNOWN;
        }
    }

    public Slice<Transaction> findByBlock(final String block, final Pageable pageable) {
        return transactionRepository.findByBlockHash(block, pageable);
    }

    public Slice<Transaction> findByBlockAndAddress(final String block, final String address, final Pageable pageable) {
        return transactionRepository.findByAddressFromOrToAndBlockHash(address, block, pageable);
    }

    @Transactional
    public Transaction reindex(final String txId) {
        log.info("reindexing " + txId);
        transactionRepository.findOne(txId)
                .ifPresent(x -> transactionRepository.delete(x));
        return getInternalTransaction(txId).toBlocking().first();
    }
}
