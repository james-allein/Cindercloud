package cloud.cinder.cindercloud.arcane.address.rest.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class GenerateAddressResponse {

    private String address;
    private String owner;

}
