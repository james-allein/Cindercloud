package cloud.cinder.cindercloud.credentials.service;

import cloud.cinder.cindercloud.credential.domain.LeakedCredential;
import cloud.cinder.cindercloud.credentials.repository.LeakedCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CredentialService {

    @Autowired
    private LeakedCredentialRepository leakedCredentialRepository;

    @Transactional
    public void saveLeakedCredential(final LeakedCredential leakedCredential) {
        if (!leakedCredentialRepository.exists(leakedCredential.getAddress())) {
            leakedCredentialRepository.save(leakedCredential);
        }
    }

}
