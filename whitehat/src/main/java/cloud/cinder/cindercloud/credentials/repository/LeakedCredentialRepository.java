package cloud.cinder.cindercloud.credentials.repository;


import cloud.cinder.cindercloud.credential.domain.LeakedCredential;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface LeakedCredentialRepository extends JpaRepository<LeakedCredential, String> {

    @Query("select lc from LeakedCredential lc")
    Stream<LeakedCredential> streamAll();

    Optional<LeakedCredential> findByAddressLike(String address);
}
