package cloud.cinder.cindercloud.wallet.controller.command.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEtherTransactionCommand {

    @NotEmpty
    private String to;
    private String gasPrice;
    @Min(21000)
    private BigInteger gasLimit = BigInteger.valueOf(31000);
    private double amount;

    public BigInteger amountInWei() {
        final BigDecimal bigDecimal = new BigDecimal(amount);
        final BigDecimal weiValue = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10, 18)));
        return new BigInteger(weiValue.toPlainString());
    }

    public BigInteger gasPriceInWei() {
        final BigDecimal bigDecimal = new BigDecimal(gasPrice);
        final BigDecimal weiValue = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10, 9)));
        return new BigInteger(weiValue.toPlainString());
    }
}
