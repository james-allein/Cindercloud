package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    public List<Transaction> save(final List<Transaction> transactions) {
        return transactionRepository.save(transactions);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getLastTransactions(Pageable pageable) {
        return transactionRepository.findAllOrOrderByBlockTimestamp(pageable);
    }
}
