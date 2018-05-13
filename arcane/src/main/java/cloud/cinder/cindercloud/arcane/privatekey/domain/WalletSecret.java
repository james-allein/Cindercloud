package cloud.cinder.cindercloud.arcane.privatekey.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.vault.repository.mapping.Secret;

import javax.persistence.Id;

@Secret
@NoArgsConstructor
@Data
public class WalletSecret {

    @Id
    private String id;

    private String privateKey;
    private String address;
    private String owner;

    @Builder
    public WalletSecret(final String privateKey,
                        final String address,
                        final String owner) {
        this.privateKey = privateKey;
        this.address = address;
        this.owner = owner;
    }
}
