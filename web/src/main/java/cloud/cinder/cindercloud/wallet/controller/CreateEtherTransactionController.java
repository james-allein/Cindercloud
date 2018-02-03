package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.security.domain.AuthenticationType;
import cloud.cinder.cindercloud.wallet.controller.command.ConfirmEtherTransactionCommand;
import cloud.cinder.cindercloud.wallet.controller.command.CreateEtherTransactionCommand;
import cloud.cinder.cindercloud.wallet.service.AuthenticationService;
import cloud.cinder.cindercloud.wallet.service.Web3TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/wallet")
public class CreateEtherTransactionController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private Web3TransactionService web3TransactionService;

    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        modelMap.put("createEtherTransactionCommand", new CreateEtherTransactionCommand());
        return "wallets/send";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String createTransaction(@Valid @ModelAttribute("createEtherTransactionCommand") final CreateEtherTransactionCommand createEtherTransactionCommand,
                                    final BindingResult bindingResult,
                                    final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return "wallets/send";
        } else {
            modelMap.addAttribute("authenticationType", authenticationService.getType());
            modelMap.put("confirm", new ConfirmEtherTransactionCommand(
                    createEtherTransactionCommand.getTo(),
                    createEtherTransactionCommand.getGasPrice(),
                    createEtherTransactionCommand.gasPriceInWei(),
                    createEtherTransactionCommand.getGasLimit(),
                    createEtherTransactionCommand.getAmount(),
                    createEtherTransactionCommand.amountInWei()
            ));
            return "wallets/confirm";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm")
    public String confirmTransaction(@Valid @ModelAttribute("confirm") final ConfirmEtherTransactionCommand confirmEtherTransactionCommand,
                                     final BindingResult bindingResult,
                                     final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to confirm your transaction");
            return "redirect:/wallet/send";
        } else {
            if (authenticationService.getType().equals(AuthenticationType.CINDERCLOUD)) {
                //do transaction
                try {
                    final String transactionHash = web3TransactionService.submitTransaction(confirmEtherTransactionCommand);
                    redirectAttributes.addFlashAttribute("success", "Your transaction has been submitted to the network: " + transactionHash);
                    return "redirect:/wallet/send";
                } catch (final AuthenticationException e) {
                    throw e;
                } catch (final Exception ex) {
                    redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to execute your transaction, please try again");
                    return "redirect:/wallet/send";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to execute your transaction, please try again");
                return "redirect:/wallet/send";
            }
        }
    }
}
