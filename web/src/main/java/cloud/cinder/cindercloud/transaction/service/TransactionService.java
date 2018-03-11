package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.cindercloud.block.domain.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.etherscan.EtherscanService;
import cloud.cinder.cindercloud.transaction.domain.Transaction;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private EtherscanService etherscanService;


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

    private Transaction enrichWithSpecialAddresses(final Transaction tx) {
        addressService.findByAddress(tx.getFromAddress()).ifPresent(tx::setSpecialFrom);
        if (tx.isContractCreation()) {
            addressService.findByAddress(tx.getCreates()).ifPresent(tx::setSpecialTo);
        } else {
            addressService.findByAddress(tx.getToAddress()).ifPresent(tx::setSpecialTo);
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

    public Slice<Transaction> findByBlock(final String block, final Pageable pageable) {
        return transactionRepository.findByBlockHash(block, pageable);
    }

    public Slice<Transaction> findByBlockAndAddress(final String block, final String address, final Pageable pageable) {
        return transactionRepository.findByAddressFromOrToAndBlockHash(address, block, pageable);
    }
}
