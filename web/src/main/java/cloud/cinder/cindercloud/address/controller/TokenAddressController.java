package cloud.cinder.cindercloud.address.controller;

import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.cryptocompare.service.TokenPriceService;
import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.dto.TokenTransferDto;
import cloud.cinder.cindercloud.token.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/token")
public class TokenAddressController {

    private TokenService tokenService;
    private TokenPriceService tokenPriceService;

    public TokenAddressController(final TokenService tokenService,
                                  final TokenPriceService tokenPriceService) {
        this.tokenService = tokenService;
        this.tokenPriceService = tokenPriceService;
    }

    @RequestMapping(value = "/{address}")
    public String byAddress(@PathVariable("address") final String address,
                            final ModelMap modelMap) {
        final Optional<Token> byAddress = tokenService.findByAddress(address);
        if (byAddress.isPresent()) {
            modelMap.put("token", byAddress.get());
            modelMap.put("price_eur", tokenPriceService.getPriceAsString(byAddress.get().getSymbol(), Currency.EUR));
            modelMap.put("price_usd", tokenPriceService.getPriceAsString(byAddress.get().getSymbol(), Currency.USD));
            modelMap.put("price_eth", tokenPriceService.getPriceAsString(byAddress.get().getSymbol(), Currency.ETH));
            return "addresses/token";
        } else {
            return "redirect:/address/" + address;
        }
    }

    @RequestMapping(value = "/{address}/transfers")
    public String getTokenTransfers(@PathVariable("address") final String address,
                                    final ModelMap modelMap) {
        List<TokenTransferDto> tokens = tokenService.findByToken(address);
        modelMap.put("tokenTransfers", tokens);
        return "addresses/tokens :: transfers";
    }
}
