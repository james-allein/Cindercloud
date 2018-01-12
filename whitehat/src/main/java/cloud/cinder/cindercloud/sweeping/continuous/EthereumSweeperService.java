package cloud.cinder.cindercloud.sweeping.continuous;

import cloud.cinder.cindercloud.credentials.service.CredentialService;
import cloud.cinder.cindercloud.sweeping.EthereumSweeper;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
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
    private Timer jobtimer;


    @Autowired
    public EthereumSweeperService(MetricRegistry metricRegistry) {
        this.jobtimer = metricRegistry.timer("sweeper");
    }

    @Scheduled(fixedDelay = 1000)
    public void sweepKnownAddresses() {
        Timer.Context timer = jobtimer.time();
        try {
            leakedCredentialRepository.streamAll()
                    .forEach(x -> ethereumSweeper.sweep(x.getPrivateKey()));
            timer.stop();
        } catch (final Exception ex) {
            log.debug("Exception during execution of sweeper-timer");
        }
    }

    @Async
    public void sweepEthereum(final String address) {
        leakedCredentialRepository.findByAddress(address).ifPresent(credential -> {
            log.debug("[SweepAfterTransaction] Looks like someone sent some eth to {}", address);
            ethereumSweeper.sweep(credential.getPrivateKey());
        });
    }
}
