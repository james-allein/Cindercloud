package cloud.cinder.cindercloud.wallet.controller.command.confirm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmTokenTransactionCommand {

    private String to;
    private String gasPrice;
    private BigInteger gasPriceInWei;
    private String tokenAddress;
    @Min(21000)
    private BigInteger gasLimit = BigInteger.valueOf(31000);
    private double amount;
    private BigInteger amountInWei;
}
