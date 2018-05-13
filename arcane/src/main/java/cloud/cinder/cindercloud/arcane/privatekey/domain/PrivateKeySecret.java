package cloud.cinder.cindercloud.arcane.privatekey.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.vault.repository.mapping.Secret;

import javax.persistence.Id;

@Secret
@Data
public class PrivateKeySecret {

    @Id
    private String id;

    private String privateKey;
    private String address;

    @Builder
    public PrivateKeySecret(final String privateKey, final String address) {
        this.privateKey = privateKey;
        this.address = address;
    }
}
