package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.erc20.controller.dto.AddressTokenDto;
import cloud.cinder.cindercloud.security.domain.AuthenticationType;
import cloud.cinder.cindercloud.token.service.ERC20Service;
import cloud.cinder.cindercloud.token.service.TokenService;
import cloud.cinder.cindercloud.wallet.controller.command.confirm.ConfirmTokenTransactionCommand;
import cloud.cinder.cindercloud.wallet.controller.command.create.CreateTokenTransactionCommand;
import cloud.cinder.cindercloud.wallet.service.AuthenticationService;
import cloud.cinder.cindercloud.wallet.service.Web3TransactionService;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wallet/tokens")
public class CreateTokenTransactionController {

    private final DecimalFormat formatter = new DecimalFormat("0.######");


    private final AuthenticationService authenticationService;
    private final Web3TransactionService web3TransactionService;
    private final TokenService tokenService;
    private final ERC20Service erc20Service;

    public CreateTokenTransactionController(final AuthenticationService authenticationService,
                                            final Web3TransactionService web3TransactionService,
                                            final TokenService tokenService,
                                            final ERC20Service erc20Service) {
        this.authenticationService = authenticationService;
        this.web3TransactionService = web3TransactionService;
        this.tokenService = tokenService;
        this.erc20Service = erc20Service;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        modelMap.put("createTokenTransactionCommand", new CreateTokenTransactionCommand());
        return "wallets/send-tokens";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String getMyTokens(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AddressTokenDto> tokens = tokenService.findAll()
                .stream()
                .map(x -> {
                    double rawBalance = erc20Service.balanceOf(address, x.getAddress()).doubleValue();
                    return new AddressTokenDto(x, formatter.format(rawBalance), rawBalance);
                })
                .filter(x -> x.getRawBalance() > 0)
                .collect(Collectors.toList());
        modelMap.put("availableTokens", tokens);
        return "components/profile :: tokenSelection";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String createTransaction(@Valid @ModelAttribute("createEtherTransactionCommand") final CreateTokenTransactionCommand createEtherTransactionCommand,
                                    final BindingResult bindingResult,
                                    final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return "wallets/sendTokens";
        } else {
            modelMap.addAttribute("authenticationType", authenticationService.getType());
            modelMap.put("confirm", new ConfirmTokenTransactionCommand(
                    createEtherTransactionCommand.getTo(),
                    createEtherTransactionCommand.getGasPrice(),
                    createEtherTransactionCommand.gasPriceInWei(),
                    createEtherTransactionCommand.getTokenAddress(),
                    createEtherTransactionCommand.getGasLimit(),
                    createEtherTransactionCommand.getAmount(),
                    createEtherTransactionCommand.amountInWei()
            ));
            return "wallets/confirmTokens";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm")
    public String confirmTransaction(@Valid @ModelAttribute("confirm") final ConfirmTokenTransactionCommand confirmEtherTransactionCommand,
                                     final BindingResult bindingResult,
                                     final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to confirm your transaction");
            return "redirect:/wallet/tokens/send";
        } else {
            if (authenticationService.getType().equals(AuthenticationType.CINDERCLOUD)) {
                try {
                    final String transactionHash = web3TransactionService.submitTokenTransaction(confirmEtherTransactionCommand);
                    redirectAttributes.addFlashAttribute("success", "Your transaction has been submitted to the network: " + transactionHash);
                    return "redirect:/wallet/tokens/send";
                } catch (final AuthenticationException e) {
                    throw e;
                } catch (final Exception ex) {
                    redirectAttributes.addFlashAttribute("error", ex.getMessage());
                    return "redirect:/wallet/tokens/send";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to execute your transaction, please try again");
                return "redirect:/wallet/tokens/send";
            }
        }
    }


}
