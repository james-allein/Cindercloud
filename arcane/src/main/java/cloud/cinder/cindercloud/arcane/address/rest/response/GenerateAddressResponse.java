package cloud.cinder.cindercloud.arcane.address.rest.response;

import cloud.cinder.cindercloud.arcane.secret.domain.WalletType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateAddressResponse {

    private String address;
    private String owner;
    private WalletType walletType;

}
