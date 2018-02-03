package cloud.cinder.cindercloud.wallet.controller.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEtherTransactionCommand {

    @NotEmpty
    private String to;
    @Min(1)
    private BigInteger gasPrice;
    @Min(21000)
    private BigInteger gasLimit = BigInteger.valueOf(31000);
    private double amount;

    public String amountInWei() {
        final BigDecimal bigDecimal = new BigDecimal(amount);
        final BigDecimal weiValue = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10, 18)));
        return weiValue.toPlainString();
    }

    public String gasPriceInWei() {
        final BigDecimal bigDecimal = new BigDecimal(gasPrice);
        final BigDecimal weiValue = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10, 9)));
        return weiValue.toPlainString();
    }
}
