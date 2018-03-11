package cloud.cinder.cindercloud.token.rest;

import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/rest/tokens")
public class TokenRestController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(method = GET)
    public List<Token> tokens() {
        return tokenService.findAll();
    }

}
