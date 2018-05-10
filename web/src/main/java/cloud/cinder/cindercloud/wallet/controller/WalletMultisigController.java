package cloud.cinder.cindercloud.wallet.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/wallet/multisig")
public class WalletMultisigController {

    @GetMapping
    public String findAll(final ModelMap modelMap) {
        modelMap.put("address", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "wallets/multisig/overview";
    }

    @GetMapping(value = "/{address}")
    public String access(@PathVariable("address") String address,
                         final ModelMap modelMap) {
        modelMap.put("address", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        modelMap.put("multisigAddress", address);
        return "wallets/multisig/multisig";
    }

    @PostMapping(value = "/{address}/add")
    public String addMultisig(@PathVariable("address") String address) {
        return "";
    }

    @PostMapping(value = "/{address}/remove")
    public String removeMultisig(@PathVariable("address") String address) {
        return "";
    }
}
