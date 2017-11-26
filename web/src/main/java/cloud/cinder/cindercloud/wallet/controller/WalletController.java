package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.cindercloud.security.domain.PrivateKeyAuthentication;
import cloud.cinder.cindercloud.utils.WeiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigInteger;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final BigInteger balance = addressService.getBalance(address).toBlocking().first();
        modelMap.put("balance", WeiUtils.format(balance));
        modelMap.put("hasBalance", balance.longValue() != 0);
        modelMap.put("address", address);
        return "wallets/index";
    }

    private void requireAuthenticated() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            throw new IllegalArgumentException("Not authenticated");
        }
    }

}
