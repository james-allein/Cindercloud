package cloud.cinder.cindercloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
public class Whitehat {

    private static final Logger log = LoggerFactory.getLogger(Whitehat.class);

    public static void main(String[] args) throws UnknownHostException {
        final SpringApplication app = new SpringApplication(Whitehat.class);
        final Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getActiveProfiles());
    }
}
