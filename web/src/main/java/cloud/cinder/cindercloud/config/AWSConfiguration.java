package cloud.cinder.cindercloud.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

    @Bean
    AWSCredentialsProvider credProvider(@Value("${cloud.aws.credentials.accessKeyId") final String id,
                                        @Value("${cloud.aws.credentials.secretKey}") final String secret) {
        return new AWSStaticCredentialsProvider(new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return id;
            }

            @Override
            public String getAWSSecretKey() {
                return secret;
            }
        });
    }

}
