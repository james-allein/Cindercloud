package cloud.cinder.cindercloud.credentials.repository;


import cloud.cinder.cindercloud.credential.domain.LeakedCredential;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;

public interface LeakedCredentialRepository extends JpaRepository<LeakedCredential, String> {
}
