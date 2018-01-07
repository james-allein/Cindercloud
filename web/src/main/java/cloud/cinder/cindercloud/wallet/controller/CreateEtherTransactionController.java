package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.security.domain.PrivateKeyAuthentication;
import cloud.cinder.cindercloud.wallet.controller.command.CreateEtherTransactionCommand;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/wallet")
public class CreateEtherTransactionController {


    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public String index(final ModelMap modelMap) {
        requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        modelMap.put("createEtherTransactionCommand", new CreateEtherTransactionCommand());
        return "wallets/send";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String createTransaction(@ModelAttribute("createEtherTransactionCommand") final CreateEtherTransactionCommand createEtherTransactionCommand,
                                    final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "wallets/send";
        } else {
            return "wallets/confirm";
        }
    }

    private void requireAuthenticated() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            throw new IllegalArgumentException("Not authenticated");
        }
    }
}
