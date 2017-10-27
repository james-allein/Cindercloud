package cloud.cinder.cindercloud.config;

import cloud.cinder.cindercloud.coinmarketcap.client.CoinMarketCapClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public CoinMarketCapClient provideCoinMarketcapClient() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        ResponseEntityDecoder decoder = new ResponseEntityDecoder(new SpringDecoder(objectFactory));
        return Feign.builder()
                .decoder(decoder)
                .target(CoinMarketCapClient.class, "https://api.coinmarketcap.com/v1");
    }
}
