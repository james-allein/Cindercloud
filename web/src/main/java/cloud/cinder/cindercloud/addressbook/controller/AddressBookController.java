package cloud.cinder.cindercloud.addressbook.controller;

import cloud.cinder.cindercloud.addressbook.controller.model.NewContactModel;
import cloud.cinder.cindercloud.addressbook.service.AddressBookService;
import cloud.cinder.cindercloud.wallet.service.AuthenticationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/wallet/address-book")
public class AddressBookController {

    private final AuthenticationService authenticationService;
    private final AddressBookService addressBookService;

    public AddressBookController(final AuthenticationService authenticationService,
                                 final AddressBookService addressBookService) {
        this.authenticationService = authenticationService;
        this.addressBookService = addressBookService;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        modelMap.put("contacts", addressBookService.getContacts(authenticationService.getAddress()));
        modelMap.put("newContactModel", new NewContactModel());
        return "wallets/address-book";
    }

    @PostMapping("/wallet/address-book/new")
    public String newContact(@ModelAttribute("newContactModel") @Valid final NewContactModel newContactModel,
                             final BindingResult bindingResult,
                             final ModelMap modelMap,
                             final RedirectAttributes redirectAttributes) {
        authenticationService.requireAuthenticated();
        if (bindingResult.hasErrors()) {
            return "wallets/address-book";
        } else {
            try {
                addressBookService.addContact(authenticationService.getAddress(), newContactModel.getAddress(), newContactModel.getNickname());
                redirectAttributes.addAttribute("success", "Successfully added your new contact");
                return "redirect:/wallet/address-book";
            } catch (final Exception ex) {
                modelMap.put("error", ex.getMessage());
                return "wallets/address-book";
            }
        }
    }

}
