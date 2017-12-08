package cloud.cinder.cindercloud.config;

import cloud.cinder.cindercloud.coinmarketcap.client.CoinMarketCapClient;
import cloud.cinder.cindercloud.etherscan.EtherscanClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cloud.cinder.etherscan-api}")
    private String etherscanApi;
    @Value("${cloud.cinder.etherscan-api-key}")
    private String etherscanApiKey;

    @Bean
    public CoinMarketCapClient provideCoinMarketcapClient() {
        final HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        final ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        final ResponseEntityDecoder decoder = new ResponseEntityDecoder(new SpringDecoder(objectFactory));
        return Feign.builder()
                .decoder(decoder)
                .target(CoinMarketCapClient.class, "https://api.coinmarketcap.com/v1");
    }

    @Bean
    public EtherscanClient provideEtherscanClient() {
        final HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        final ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        final ResponseEntityDecoder decoder = new ResponseEntityDecoder(new SpringDecoder(objectFactory));

        return Feign.builder()
                .decoder(decoder)
                .encoder(new SpringEncoder(objectFactory))
                .logLevel(Logger.Level.FULL)
                .logger(new Logger.JavaLogger())
                .requestInterceptor(template -> template.query("apikey", etherscanApiKey))
                .target(EtherscanClient.class, etherscanApi);
    }

}
