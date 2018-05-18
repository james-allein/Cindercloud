package cloud.cinder.cindercloud.arcane.address.rest;

import cloud.cinder.cindercloud.arcane.address.rest.request.GenerateAddressRequest;
import cloud.cinder.cindercloud.arcane.address.rest.response.GenerateAddressResponse;
import cloud.cinder.cindercloud.arcane.address.rest.response.ListAddressesResponse;
import cloud.cinder.cindercloud.arcane.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/address")
public class AddressRestController {

    @Autowired
    private AddressService addressService;

    @PostMapping(value = "/generate")
    public GenerateAddressResponse generate(@RequestBody final GenerateAddressRequest generateAddressRequest) {
        final String generatedAddress = addressService.generate(generateAddressRequest.getOwner(), generateAddressRequest.getWalletType());
        return GenerateAddressResponse.builder()
                .address(generatedAddress)
                .walletType(generateAddressRequest.getWalletType())
                .owner(generateAddressRequest.getOwner())
                .build();
    }

    @GetMapping(value = "/list")
    public ListAddressesResponse listAddresses(@PathParam("owner") final String owner) {
        return
                ListAddressesResponse.builder()
                        .addresses(addressService.list(owner))
                        .build();
    }
}
