package cloud.cinder.cindercloud.whitehat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/whitehat")
@Controller
public class WhitehatController {

    @RequestMapping(method = RequestMethod.GET, value = "/faq")
    public String faq() {
        return "whitehat/faq";
    }
}
