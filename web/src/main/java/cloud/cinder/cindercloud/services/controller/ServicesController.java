package cloud.cinder.cindercloud.services.controller;

import cloud.cinder.cindercloud.services.controller.dto.RequestDevelopmentCommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/services")
public class ServicesController {


    public ServicesController() {
    }

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
        modelMap.putIfAbsent("requestDevelopmentCommand", new RequestDevelopmentCommand());
        return "services/security-and-development";
    }

    @PostMapping("/smart-contract-development-and-security")
    public String doRequestDevelopment(@ModelAttribute("requestDevelopmentCommand") final RequestDevelopmentCommand requestDevelopmentCommand,
                                       final BindingResult bindingResult,
                                       final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return requestDevelopment(modelMap);
        }

        return "";
    }
}
