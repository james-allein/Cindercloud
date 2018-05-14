package cloud.cinder.cindercloud.arcane.secret.repository;

import cloud.cinder.cindercloud.arcane.secret.domain.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretRepository extends JpaRepository<Secret, String> {
}
