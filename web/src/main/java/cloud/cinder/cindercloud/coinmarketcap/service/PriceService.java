package cloud.cinder.cindercloud.coinmarketcap.service;

import cloud.cinder.cindercloud.coinmarketcap.client.CoinMarketCapClient;
import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.coinmarketcap.dto.TickerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

@Component
public class PriceService {

    @Autowired
    private CoinMarketCapClient coinMarketCapClient;

    final DecimalFormat df = new DecimalFormat("##.00");

    @Cacheable(value = "price_as_string", key = "#currency")
    public String getPriceAsString(final Currency currency) {
        final List<TickerResult> results = coinMarketCapClient.getTickerById("ethereum");
        if (results.size() > 0) {
            return df.format(new Double(results.get(0).forCurrency(currency)));
        } else {
            return "unknown";
        }
    }


    @Cacheable(value = "price", key = "#currency")
    public double getPrice(final Currency currency) {
        final List<TickerResult> results = coinMarketCapClient.getTickerById("ethereum");
        if (results.size() > 0) {
            try {
                final String result = results.get(0).forCurrency(currency);
                return Double.valueOf(result);
            } catch (final Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
