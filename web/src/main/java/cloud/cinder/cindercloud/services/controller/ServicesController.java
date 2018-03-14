package cloud.cinder.cindercloud.services.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/services")
public class ServicesController {

    @GetMapping
    public String index() {
        return "services/index";
    }

    @GetMapping("/looking-for-advice")
    public String requestAdvice(final ModelMap modelMap) {
        return "services/advisor";
    }

    @GetMapping("/smart-contract-development-and-security")
    public String requestDevelopment(final ModelMap modelMap) {
        return "services/security-and-development";
    }
}
