package cloud.cinder.cindercloud.arcane.privatekey.repository;

import cloud.cinder.cindercloud.arcane.privatekey.domain.PrivateKeySecret;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrivateKeySecretRepository extends CrudRepository<PrivateKeySecret, String> {

    List<PrivateKeySecret> findByOwner(final String owner);
}
