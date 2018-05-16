package cloud.cinder.cindercloud.arcane.secret.service;

import cloud.cinder.cindercloud.arcane.privatekey.repository.WalletSecretRepository;
import cloud.cinder.cindercloud.arcane.secret.repository.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnProperty(name = "cloud.cinder.cleanup-at-start", havingValue = "true")
public class SecretCleanupService {

    @Autowired
    private SecretRepository secretRepository;
    @Autowired
    private WalletSecretRepository walletSecretRepository;

    @PostConstruct
    public void init() {
        secretRepository.findAll()
                .stream()
                .filter(x -> !walletSecretRepository.existsById(x.getSecretId()))
                .forEach(x -> secretRepository.delete(x));
    }
}
