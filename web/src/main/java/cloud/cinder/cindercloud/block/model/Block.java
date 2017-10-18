package cloud.cinder.cindercloud.block.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blocks")
@Entity
public class Block {

    @Id
    private String hash;
    private String mixHash;
    private BigInteger height;
    private String parentHash;
    @Column(name = "sha3_uncles")
    private String sha3Uncles;
    private String minedBy;
    private BigInteger difficulty;
    private BigInteger difficultyTotal;
    private BigInteger gasLimit;
    private BigInteger gasUsed;
    private String receiptsRoot;
    private BigInteger size;
    private BigInteger timestamp;
    private BigInteger nonce;
    private String extraData;

    public String prettyHash() {
        return hash.substring(0, 25) + "...";
    }

    public String prettyTimestamp() {
        final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp.longValue(), 0, ZoneOffset.UTC);
        final PrettyTime prettyTime = new PrettyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(Date.from(localDateTime.atOffset(ZoneOffset.UTC).toInstant()));
    }
}
