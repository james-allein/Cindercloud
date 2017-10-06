package cloud.cinder.cindercloud.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

import static cloud.cinder.cindercloud.utils.WeiUtils.format;

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
    private BigInteger value;
    private String input;
    private BigInteger nonce;
    private String s;
    private String r;
    private Integer v;
    @Column(name = "from_address")
    private String fromAddress;
    @Column(name = "to_address")
    private String toAddress;

    public String formattedValue() {
        return format(value);
    }

    public String formattedGasPrice() {
        return format(gasPrice);
    }
}

