package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.security.domain.Web3Authentication;
import cloud.cinder.cindercloud.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/web3/login")
public class Web3LoginController {


    @Autowired
    private WalletService walletService;

    @RequestMapping(method = GET)
    public String index() {
        return "wallets/web3";
    }

    @RequestMapping(method = POST)
    public @ResponseBody
    String login(@RequestParam("address") final String address) {
        final String validatedAddress = walletService.web3Login(address);
        populateSecurityContext(validatedAddress);
        return "OK";
    }

    private void populateSecurityContext(final String address) {
        SecurityContextHolder.getContext().setAuthentication(new Web3Authentication(address));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
    }
}
