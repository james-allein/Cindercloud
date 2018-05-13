package cloud.cinder.cindercloud.arcane.privatekey.repository;

import cloud.cinder.cindercloud.arcane.privatekey.domain.WalletSecret;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WalletSecretRepository extends CrudRepository<WalletSecret, String> {

    List<WalletSecret> findByOwner(final String owner);
}
