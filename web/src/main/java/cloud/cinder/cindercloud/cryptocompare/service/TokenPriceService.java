package cloud.cinder.cindercloud.cryptocompare.service;

import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import org.springframework.stereotype.Service;

@Service
public class TokenPriceService {

    private final CryptoCompareGateway cryptoCompareGateway;

    public TokenPriceService(final CryptoCompareGateway cryptoCompareGateway) {
        this.cryptoCompareGateway = cryptoCompareGateway;
    }

    public String getPrice(final String symbol, final Currency currency) {
        return cryptoCompareGateway.getPrice(symbol).getFor(currency);
    }
}
