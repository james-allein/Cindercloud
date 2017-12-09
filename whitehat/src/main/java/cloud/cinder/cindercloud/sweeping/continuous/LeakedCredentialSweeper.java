package cloud.cinder.cindercloud.sweeping.continuous;

import cloud.cinder.cindercloud.credentials.service.CredentialService;
import cloud.cinder.cindercloud.sweeping.Sweeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LeakedCredentialSweeper {

    @Autowired
    private Sweeper sweeper;
    @Autowired
    private CredentialService leakedCredentialRepository;

    @Scheduled(fixedDelay = 1000)
    public void sweepKnownAddresses() {
        leakedCredentialRepository.streamAll()
                .forEach(x -> sweeper.sweep(x.getPrivateKey()));
    }
}
