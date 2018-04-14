package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.service.ERC20Service;
import cloud.cinder.cindercloud.token.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wallet/kyber")
@Slf4j
public class WalletKyberController {

    private final TokenService tokenService;
    private final ERC20Service erc20Service;

    public WalletKyberController(final TokenService tokenService,
                                 final ERC20Service erc20Service) {
        this.tokenService = tokenService;
        this.erc20Service = erc20Service;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        modelMap.put("address", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "wallets/kyber";
    }

    @RequestMapping(value = "/{address}/sources")
    public @ResponseBody
    List<Token> getTokens(@PathVariable("address") String address) {
        return tokenService.findAll()
                .stream()
                .filter(x -> {
                    final double rawBalance = erc20Service.balanceOf(address, x.getAddress()).doubleValue();
                    return rawBalance > 0;
                })
                .collect(Collectors.toList());
    }
}
