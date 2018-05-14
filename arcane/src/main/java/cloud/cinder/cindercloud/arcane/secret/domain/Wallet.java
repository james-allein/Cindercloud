package cloud.cinder.cindercloud.arcane.secret.domain;

import cloud.cinder.cindercloud.arcane.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "wallets")
@Entity
@Data
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secret_id")
    private String secretId;

    @Column(name = "address")
    private String address;

    @Column(name = "wallet_type")
    @Enumerated(value = EnumType.STRING)
    private WalletType walletType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @Builder
    public Wallet(final String secretId, final String address, final WalletType walletType, final User user) {
        this.secretId = secretId;
        this.address = address;
        this.walletType = walletType;
        this.user = user;
    }
}
