package cloud.cinder.cindercloud.address.controller;

import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(value = "/token")
public class TokenAddressController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/{address}")
    public String byAddress(@PathVariable("address") final String address,
                            final ModelMap modelMap) {
        Optional<Token> byAddress = tokenService.findByAddress(address);
        if (byAddress.isPresent()) {
            modelMap.put("token", byAddress.get());
            return "addresses/token";
        } else {
            return "redirect:/address/" + address;
        }
    }
}
