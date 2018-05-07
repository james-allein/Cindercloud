package cloud.cinder.cindercloud.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.Executors;

@Configuration
public class Web3Config {

    @Bean
    @Primary
    public Web3j provideWeb3J(final Web3jService web3jService) {
        return Web3j.build(web3jService,
                15 * 1000, Executors.newScheduledThreadPool(30));
    }

    @Bean
    @Qualifier("local")
    public Web3j provideLocalWeb3(@Qualifier("local") final Web3jService web3jService) {
        return Web3j.build(web3jService,
                15 * 1000, Executors.newScheduledThreadPool(30));
    }

    @Bean
    @Primary
    public Web3jService provideWeb3JService(@Value("${cloud.cinder.ethereum.endpoint.url}") final String endpoint) {
        return new HttpService(endpoint);
    }

    @Bean
    @Qualifier("local")
    public Web3jService provideInfuraEndpoint(@Value("${cloud.cinder.ethereum.endpoint.local-url}") final String endpoint) {
        return new HttpService(endpoint);
    }
}