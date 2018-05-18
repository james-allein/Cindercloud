package cloud.cinder.cindercloud.arcane.secret.repository;

import cloud.cinder.cindercloud.arcane.secret.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SecretRepository extends JpaRepository<Wallet, String> {

    List<Wallet> findByUserExternalId(@Param("externalId") final String externalId);

    Optional<Wallet> findByAddress(@Param("address") final String address);

}
