package cloud.cinder.cindercloud.erc20.controller;

import cloud.cinder.cindercloud.erc20.service.ERC20Service;
import cloud.cinder.cindercloud.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/address/{address}")
public class ERC20Controller {

    final DecimalFormat formatter = new DecimalFormat("##.######");

    @Autowired
    private ERC20Service erc20Service;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/tokens")
    public String balanceOf(final @PathVariable("address") String address, final ModelMap modelmap) {
        List<AddressTokenDto> tokens = tokenService.findAll()
                .stream()
                .map(x -> {
                    double rawBalance = erc20Service.balanceOf(address, x.getAddress()).doubleValue();
                    return new AddressTokenDto(x, formatter.format(rawBalance), rawBalance);
                })
                .filter(x -> x.getRawBalance() > 0)
                .collect(Collectors.toList());

        modelmap.put("tokens", tokens);
        return "addresses/tokens :: tokenlist";
    }

}
