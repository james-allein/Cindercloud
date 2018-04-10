package cloud.cinder.cindercloud.cryptocompare.client;

import cloud.cinder.cindercloud.cryptocompare.dto.PriceResultDto;
import feign.Param;
import feign.RequestLine;

public interface CryptoCompareClient {

    @RequestLine("GET /data/price?fsym={symbol}&tsyms=EUR,USD,BTC,ETH")
    PriceResultDto getPrice(final @Param("symbol") String symbol);

}
