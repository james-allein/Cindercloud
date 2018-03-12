package cloud.cinder.cindercloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@SpringBootApplication
@EnableAutoConfiguration
@Slf4j
@EnableScheduling
public class Whitehat {

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
