package cloud.cinder.cindercloud.arcane.transaction.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class SignedTransactionDto {

    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private String data;
    private String signedBytes;

    @Builder
    public SignedTransactionDto(final RawTransactionDto rawTransactionDto, final String signedBytes) {
        this.nonce = rawTransactionDto.getNonce();
        this.gasPrice = rawTransactionDto.getGasPrice();
        this.gasLimit = rawTransactionDto.getGasLimit();
        this.to = rawTransactionDto.getTo();
        this.value = rawTransactionDto.getValue();
        this.data = rawTransactionDto.getData();
        this.signedBytes = signedBytes;
    }
}
