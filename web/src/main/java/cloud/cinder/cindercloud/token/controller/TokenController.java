package cloud.cinder.cindercloud.token.controller;

import cloud.cinder.cindercloud.token.model.Token;
import cloud.cinder.cindercloud.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping(value = "/tokens")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/{name}")
    public String tokenByName(@PathVariable("name") final String name) {
        final Optional<Token> tokenByName = tokenService.getTokenByName(name);
        if (tokenByName.isPresent()) {
            return "tokens/full";
        }

        final Optional<Token> tokenByAddress = tokenService.getTokenByAddress(name);
        if (tokenByAddress.isPresent()) {
            return "tokens/full";
        }

        else {
            return "tokens/address";
        }
    }

}
