package cloud.cinder.cindercloud.sweeping.continuous;

import cloud.cinder.cindercloud.credentials.service.CredentialService;
import cloud.cinder.cindercloud.sweeping.EthereumSweeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EthereumSweeperService {

    @Autowired
    private EthereumSweeper ethereumSweeper;
    @Autowired
    private CredentialService leakedCredentialRepository;

    @Scheduled(fixedDelay = 1000)
    public void sweepKnownAddresses() {
        leakedCredentialRepository.streamAll()
                .forEach(x -> ethereumSweeper.sweep(x.getPrivateKey()));
    }

    @Async
    public void sweepEthereum(final String address) {
        leakedCredentialRepository.findByAddress(address).ifPresent(credential -> ethereumSweeper.sweep(credential.getPrivateKey()));
    }
}
