package cloud.cinder.cindercloud.address.controller.vo;

import cloud.cinder.cindercloud.transaction.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@Data
public class AddressVO {

    private String code;
    private String balance;
    private BigInteger transactionCount;
    private List<Transaction> transactions;

    public boolean isContract() {
        return (code != null && !code.equals("0x"));
    }
}
