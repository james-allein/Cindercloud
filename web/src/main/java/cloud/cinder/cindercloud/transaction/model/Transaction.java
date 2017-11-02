package cloud.cinder.cindercloud.transaction.model;

import com.amazonaws.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
    private BigInteger blockHeight;
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
    private String creates;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockTimestamp;

    public boolean isContractCreation() {
        return (StringUtils.isNullOrEmpty(toAddress) && !StringUtils.isNullOrEmpty(creates));
    }

    public String formattedValue() {
        return format(value);
    }

    public String formattedGasPrice() {
        return format(gasPrice);
    }

    public String prettyHash() {
        return hash.substring(0, 18) + "...";
    }

    public String prettyBlockHash() {
        return blockHash.substring(0, 18) + "...";
    }

    public String prettyFromAddress() {
        return fromAddress.substring(0, 18) + "...";
    }

    public String prettyToAddress() {
        if (isContractCreation()) {
            return creates.substring(0, 18) + "... [contract creation]";
        }
        return toAddress.substring(0, 18) + "...";
    }

    public String prettyBlockTimestamp() {
        final PrettyTime prettyTime = new PrettyTime(java.sql.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(blockTimestamp);
    }

    public Direction direction(final String address) {
        if (fromAddress.equals(address)) {
            return Direction.OUT;
        } else {
            return Direction.IN;
        }
    }

    public boolean hasInput() {
        return input != null && !input.equals("0x0");
    }

    public String inputString() {
        try {
            return new String(Hex.decode(input.substring(2)));
        } catch (Exception extraDataString) {
            return "";
        }
    }

    public enum Direction {
        IN, OUT
    }
}

