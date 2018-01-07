package cloud.cinder.cindercloud.wallet.controller.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEtherTransactionCommand {

    private String to;
    private long gasPrice;
    private long gasLimit;
    private double amount;
}
