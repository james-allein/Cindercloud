package cloud.cinder.cindercloud.arcane;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;

@Slf4j
@EnableAutoConfiguration
@EnableVaultRepositories
@SpringBootApplication
public class ArcaneApplication {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(ArcaneApplication.class);
        final Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getActiveProfiles());
    }
}
