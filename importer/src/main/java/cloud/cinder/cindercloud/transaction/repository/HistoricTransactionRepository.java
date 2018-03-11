package cloud.cinder.cindercloud.transaction.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.transaction.domain.HistoricTransaction;

public interface HistoricTransactionRepository extends JpaRepository<HistoricTransaction, String> {
}
