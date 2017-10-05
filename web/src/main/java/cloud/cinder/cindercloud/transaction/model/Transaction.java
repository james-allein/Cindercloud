package cloud.cinder.cindercloud.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private String hash;
    private String blockHash;
    private BigInteger gasPrice;
    private BigInteger gas;
    private BigInteger transactionIndex;
    private String input;
    private BigInteger nonce;
    private String s;
    private String r;
    private Integer v;
    @Column(name = "from_address")
    private String from;
    @Column(name = "to_address")
    private String to;
}
