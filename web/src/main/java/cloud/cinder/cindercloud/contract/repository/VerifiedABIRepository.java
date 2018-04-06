package cloud.cinder.cindercloud.contract.repository;

import cloud.cinder.cindercloud.contract.domain.VerifiedABI;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerifiedABIRepository extends JpaRepository<VerifiedABI, Long> {

    Optional<VerifiedABI> findByAddress(@Param("address") final String address);

}
