package cloud.cinder.cindercloud.cryptocompare.rest;

import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.cryptocompare.service.TokenPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token-price")
public class TokenPriceRestController {

    private final TokenPriceService priceService;

    public TokenPriceRestController(final TokenPriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public String getPrice(@RequestParam("symbol") final String symbol, @RequestParam(value = "currency", defaultValue = "EUR") final Currency currency) {
        return priceService.getPriceAsString(symbol, currency);
    }
}
