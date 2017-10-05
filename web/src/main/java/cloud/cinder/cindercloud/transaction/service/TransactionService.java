package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import rx.Observable;

@Service
public class TransactionService {

    @Autowired
    private Web3j web3j;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Observable<Transaction> getTransaction(final String transactionHash) {
        return transactionRepository.findOne(transactionHash)
                .map(Observable::just)
                .orElse(
                        web3j.ethGetTransactionByHash(transactionHash).observable()
                                .map(transaction -> transaction.getTransaction()
                                        .map(tx -> Transaction.builder()
                                                .blockHash(tx.getBlockHash())
                                                .from(tx.getFrom())
                                                .gas(tx.getGas())
                                                .hash(tx.getHash())
                                                .input(tx.getInput())
                                                .to(tx.getTo())
                                                .gasPrice(tx.getGasPrice())
                                                .s(tx.getS())
                                                .r(tx.getR())
                                                .v(tx.getV())
                                                .nonce(tx.getNonce())
                                                .transactionIndex(tx.getTransactionIndex())
                                                .build())
                                        .map(tx -> transactionRepository.save(tx))
                                        .orElse(null))
                );
    }

}
