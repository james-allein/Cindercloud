package cloud.cinder.cindercloud.arcane.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class RawTransactionDto {


    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private String from;
    private BigInteger value;
    private String data;

    public RawTransaction toRawTransaction() {
        return RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, to, value, data
        );
    }
}
