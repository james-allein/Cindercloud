package cloud.cinder.cindercloud.arcane.address.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListAddressesResponse {
    private List<String> addresses;
}
