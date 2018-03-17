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

    private Long id;
    private String from;
    private String to;
    private BigInteger amount;
    private String transactionid;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockTimestamp;

}
