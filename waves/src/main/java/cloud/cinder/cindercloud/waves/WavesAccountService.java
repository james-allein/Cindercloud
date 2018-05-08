package cloud.cinder.cindercloud.waves;

import cloud.cinder.cindercloud.waves.gateway.WavesGateway;
import org.springframework.stereotype.Component;

@Component
public class WavesAccountService {

    private final WavesGateway waves;

    WavesAccountService(final WavesGateway waves) {
        this.waves = waves;
    }

    public long getBalance(final String account) {
        try {
            return waves.node().getBalance(account);
        } catch (final Exception ex) {
            return 0L;
        }
    }
}
