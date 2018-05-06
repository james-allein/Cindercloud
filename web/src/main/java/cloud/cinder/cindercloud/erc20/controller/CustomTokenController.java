package cloud.cinder.cindercloud.erc20.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet/custom-tokens")
public class CustomTokenController {



    @GetMapping
    public String index(final ModelMap modelMap) {
        return
    }

}
