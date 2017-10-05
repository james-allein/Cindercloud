package cloud.cinder.cindercloud.transaction.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.transaction.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
