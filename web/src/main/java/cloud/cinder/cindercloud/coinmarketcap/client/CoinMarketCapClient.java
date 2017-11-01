package cloud.cinder.cindercloud.coinmarketcap.client;

import cloud.cinder.cindercloud.coinmarketcap.dto.TickerResult;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface CoinMarketCapClient {

    @RequestLine("GET /ticker/{id}?convert=EUR")
    List<TickerResult> getTickerById(@Param("id") final String id);
}
