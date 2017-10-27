package cloud.cinder.cindercloud.coinmarketcap.service;

import cloud.cinder.cindercloud.coinmarketcap.client.CoinMarketCapClient;
import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.coinmarketcap.dto.TickerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricingService {

    @Autowired
    private CoinMarketCapClient coinMarketCapClient;

    @Cacheable(value = "price", key = "#currency")
    public String getPrice(final Currency currency) {
        final List<TickerResult> results = coinMarketCapClient.getTickerById("ethereum");
        if (results.size() > 0) {
            return results.get(0).forCurrency(currency);
        } else {
            return "unknown";
        }
    }
}
