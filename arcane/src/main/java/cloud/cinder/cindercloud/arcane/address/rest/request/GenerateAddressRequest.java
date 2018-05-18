package cloud.cinder.cindercloud.arcane.address.rest.request;

import cloud.cinder.cindercloud.arcane.secret.domain.WalletType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateAddressRequest {

    private String owner;
    private WalletType walletType = WalletType.ETHEREUM;
}
