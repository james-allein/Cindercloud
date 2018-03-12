package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.cindercloud.transaction.domain.Transaction;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void save(final Transaction transaction) {
        if (!transactionRepository.exists(transaction.getHash())) {
            transactionRepository.save(transaction);
        }
    }

    @Transactional
    public void saveIfNotExists(final Transaction transaction) {
        if (!transactionRepository.exists(transaction.getHash())) {
            transactionRepository.save(transaction);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(final String hash) {
        return transactionRepository.findOne(hash);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getLastTransactions(Pageable pageable) {
        return transactionRepository.findAllOrOrderByBlockTimestamp(pageable);
    }
}
