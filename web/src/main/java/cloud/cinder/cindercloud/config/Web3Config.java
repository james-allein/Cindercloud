package cloud.cinder.cindercloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {

    @Bean
    Web3j provideWeb3J(@Value("${cloud.cinder.ethereum.endpoint.url}") final String endpoint) {
        return Web3j.build(new HttpService(endpoint));
    }
}