package cloud.cinder.cindercloud.arcane.privatekey.repository;

import cloud.cinder.cindercloud.arcane.privatekey.domain.WalletSecret;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletSecretRepository extends CrudRepository<WalletSecret, String> {

}
