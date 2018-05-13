package cloud.cinder.cindercloud.arcane.privatekey.repository;

import cloud.cinder.cindercloud.arcane.privatekey.domain.PrivateKeySecret;
import org.springframework.data.repository.CrudRepository;

public interface PrivateKeySecretRepository extends CrudRepository<PrivateKeySecret, String> {

}
