package cloud.cinder.cindercloud.arcane.wallet;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "wallet_addresses")
@NoArgsConstructor
public class WalletAddress {

    @Id
    private Long id;

    private String owner;
    private String address;

    @Column(name = "secret_key")
    private String secretKey;
    @Column(name = "wallet_type")
    private WalletType walletType;

    @Builder
    public WalletAddress(final String owner, final String address, final String secretKey) {
        this.owner = owner;
        this.address = address;
        this.secretKey = secretKey;
    }
}
