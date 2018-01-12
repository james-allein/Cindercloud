package cloud.cinder.cindercloud.sweeping.continuous;

import cloud.cinder.cindercloud.credentials.service.CredentialService;
import cloud.cinder.cindercloud.sweeping.EthereumSweeper;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EthereumSweeperService {

    @Autowired
    private EthereumSweeper ethereumSweeper;
    @Autowired
    private CredentialService leakedCredentialRepository;
    private Meter ethereumSweeperMeter;


    @Autowired
    public EthereumSweeperService(final MetricRegistry metricRegistry) {
        this.ethereumSweeperMeter = metricRegistry.meter("sweeper");
    }

    @Scheduled(fixedDelay = 1000)
    public void sweepKnownAddresses() {
        leakedCredentialRepository.streamAll()
                .forEach(x -> ethereumSweeper.sweep(x.getPrivateKey()));
        ethereumSweeperMeter.mark();
    }

    @Async
    public void sweepEthereum(final String address) {
        leakedCredentialRepository.findByAddress(address).ifPresent(credential -> {
            log.debug("[SweepAfterTransaction] Looks like someone sent some eth to {}", address);
            ethereumSweeper.sweep(credential.getPrivateKey());
        });
    }
}
