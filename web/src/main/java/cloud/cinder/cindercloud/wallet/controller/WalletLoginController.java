package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.security.domain.PrivateKeyAuthentication;
import cloud.cinder.cindercloud.wallet.controller.command.login.KeystoreLoginCommand;
import cloud.cinder.cindercloud.wallet.controller.command.login.PrivateKeyLoginCommand;
import cloud.cinder.cindercloud.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web3j.crypto.Credentials;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(value = "/wallet/login")
@Controller
@Slf4j
public class WalletLoginController {

    @Autowired
    private WalletService walletService;

    @RequestMapping(method = GET)
    public String index(final ModelMap modelMap) {
        modelMap.put("keystoreLoginCommand", new KeystoreLoginCommand());
        modelMap.put("privatekeyLoginCommand", new PrivateKeyLoginCommand());
        return "wallets/login";
    }

    @RequestMapping(method = POST, params = "type=keystore")
    public String loginWithKeystore(final @ModelAttribute("keystoreLoginCommand") KeystoreLoginCommand keystoreLoginCommand,
                                    final RedirectAttributes redirectAttributes) {
        try {
            final Credentials creds = walletService.login(keystoreLoginCommand.getPassword(), keystoreLoginCommand.getKeystoreValue());
            populateSecurityContext(creds);
            return "redirect:/wallet";
        } catch (final Exception ex) {
            log.debug("Error while trying to login with keystore", ex);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/wallet/login";
        }
    }

    @RequestMapping(method = POST, params = "type=privatekey")
    public String loginWithPrivateKey(final @ModelAttribute("keystoreLoginCommand") PrivateKeyLoginCommand privateKeyLoginCommand,
                                      final RedirectAttributes redirectAttributes) {
        try {
            final Credentials creds = walletService.login(privateKeyLoginCommand.getPrivateKey());
            populateSecurityContext(creds);
            return "redirect:/wallet";
        } catch (final Exception ex) {
            log.debug("Error while trying to login with private key", ex);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/wallet/login";
        }
    }

    private void populateSecurityContext(final Credentials creds) {
        SecurityContextHolder.getContext().setAuthentication(new PrivateKeyAuthentication(creds.getEcKeyPair(), creds.getAddress()));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
    }
}
