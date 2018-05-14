package cloud.cinder.cindercloud.arcane.address.rest;

import cloud.cinder.cindercloud.arcane.address.ClaimAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/address")
public class AddressRestController {

    @Autowired
    private ClaimAddressService claimAddressService;

    @RequestMapping(value = "/generate")
    public String generate() {
        return claimAddressService.claimAddress("cindercloud");
    }
}
