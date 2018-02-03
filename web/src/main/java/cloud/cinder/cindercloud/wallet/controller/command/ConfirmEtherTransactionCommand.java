package cloud.cinder.cindercloud.wallet.controller.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmEtherTransactionCommand {

    private String to;
    @Min(1)
    private BigInteger gasPrice;
    private String gasPriceInWei;
    @Min(21000)
    private BigInteger gasLimit = BigInteger.valueOf(31000);
    private double amount;
    private String amountInWei;
}
