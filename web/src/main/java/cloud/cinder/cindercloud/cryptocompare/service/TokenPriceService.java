package cloud.cinder.cindercloud.cryptocompare.service;

import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import org.springframework.stereotype.Service;

@Service
public class TokenPriceService {

    private final CryptoCompareGateway cryptoCompareGateway;

    public TokenPriceService(final CryptoCompareGateway cryptoCompareGateway) {
        this.cryptoCompareGateway = cryptoCompareGateway;
    }

    public String getPriceAsString(final String symbol, final Currency currency) {
        final String price = cryptoCompareGateway.getPrice(symbol).getFor(currency);
        return price == null ? "0.00" : price;
    }

    public double getPrice(final String symbol, final Currency currency) {
        final String price = cryptoCompareGateway.getPrice(symbol).getFor(currency);
        return price == null ? 0 : Double.valueOf(price);
    }
}
