package cloud.cinder.cindercloud.wallet.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(final HttpServletRequest httpServletRequest) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        httpServletRequest.getSession(false).invalidate();
        return "redirect:/wallet/login";
    }
}
