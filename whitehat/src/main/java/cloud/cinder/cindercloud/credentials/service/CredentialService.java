package cloud.cinder.cindercloud.credentials.service;

import cloud.cinder.cindercloud.credential.domain.LeakedCredential;
import cloud.cinder.cindercloud.credentials.repository.LeakedCredentialRepository;
import cloud.cinder.cindercloud.sweeping.Sweeper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Slf4j
public class CredentialService {

    @Autowired
    private LeakedCredentialRepository leakedCredentialRepository;
    @Autowired
    private Sweeper sweeper;

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

    private void sweep(final LeakedCredential leakedCredential) {
        log.debug("going to try to sweep {}", leakedCredential.getAddress());
        sweeper.sweep(leakedCredential.getPrivateKey());
    }

}
