package cloud.cinder.cindercloud.addressbook.controller;

import cloud.cinder.cindercloud.wallet.service.AuthenticationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet/address-book")
public class AddressBookController {

    private final AuthenticationService authenticationService;

    public AddressBookController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        return "wallets/address-book";
    }
}
