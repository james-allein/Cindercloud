package cloud.cinder.cindercloud.erc20.rest;

import cloud.cinder.cindercloud.token.service.ERC20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;

@RestController
@RequestMapping(value = "/rest/erc20/{tokenAddress}")
public class ERC20RestController {

    private final DecimalFormat formatter = new DecimalFormat("##.######");

    @Autowired
    private ERC20Service erc20Service;

    @RequestMapping(value = "/balance")
    public String balanceOf(final @RequestParam("address") String address, final @PathVariable("tokenAddress") String tokenAddress) {
        return formatter.format(erc20Service.balanceOf(address, tokenAddress).doubleValue());
    }
}
