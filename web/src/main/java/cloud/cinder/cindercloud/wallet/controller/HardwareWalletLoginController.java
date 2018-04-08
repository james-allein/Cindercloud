package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.trezor.TrezorVerificationService;
import cloud.cinder.cindercloud.wallet.controller.command.login.TrezorLoginCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hwallet")
public class HardwareWalletLoginController {

    private TrezorVerificationService trezorVerificationService;

    public HardwareWalletLoginController(final TrezorVerificationService trezorVerificationService) {
        this.trezorVerificationService = trezorVerificationService;
    }

    @RequestMapping(value = "/trezor", method = RequestMethod.POST)
    public @ResponseBody
    boolean trezor(@RequestBody TrezorLoginCommand trezorLoginCommand) {
        if (trezorVerificationService.verify(trezorLoginCommand.getHiddenChallenge(),
                trezorLoginCommand.getVisualChallenge(),
                trezorLoginCommand.getPublic_key(),
                trezorLoginCommand.getSignature())) {
            //login
            return true;
        }
        return false;
    }

}
