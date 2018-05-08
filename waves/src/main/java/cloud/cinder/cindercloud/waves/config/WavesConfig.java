package cloud.cinder.cindercloud.waves.config;

import com.wavesplatform.wavesj.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WavesConfig {

    @Bean
    public Node provideWaves() {
        try {
            return new Node("https://nodes.wavesplatform.com");
        } catch (final Exception ex) {
            log.debug("Unable to connect to node {}", ex.getMessage());
            return new Node();
        }
    }
}
