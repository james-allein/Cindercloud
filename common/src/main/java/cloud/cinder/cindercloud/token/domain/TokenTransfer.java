package cloud.cinder.cindercloud.token.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "token_transfers")
@Data
@NoArgsConstructor
public class TokenTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_address")
    private String from;
    @Column(name = "to_address")
    private String to;
    private BigInteger amount;
    @Column(name = "transaction_hash")
    private String transactionHash;
    @Column(name = "transaction_index")
    private BigInteger transactionIndex;
    @Column(name = "origin_address")
    private String originAddress;
    @Column(name = "removed")
    private boolean removed;
    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockTimestamp;
    @Column(name = "token_address")
    private String tokenAddress;
}
