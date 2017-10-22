package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import rx.Observable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private Web3j web3j;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BlockService blockService;

    @Transactional(readOnly = true)
    public Observable<Transaction> findByAddress(final String address) {
        return Observable.from(() -> transactionRepository.findByAddressFromOrTo(address).iterator());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public Observable<Transaction> getTransactionsForBlock(final String blockHash) {
        return Observable.from(transactionRepository.findAllByBlockHash(blockHash));
    }

    @Transactional
    public Observable<Transaction> getTransaction(final String transactionHash) {
        return transactionRepository.findOne(transactionHash)
                .map(Observable::just)
                .orElse(getInternalTransaction(transactionHash));
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getLastTransactions(Pageable pageable) {
        return transactionRepository.findAllOrOrderByBlockTimestamp(pageable);
    }

    private Observable<Transaction> getInternalTransaction(final String transactionHash) {
        try {
            return web3j.ethGetTransactionByHash(transactionHash)
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

    public Page<Transaction> find(final String searchKey, final Pageable pageable) {
        return transactionRepository.find(searchKey, pageable);
    }
}
