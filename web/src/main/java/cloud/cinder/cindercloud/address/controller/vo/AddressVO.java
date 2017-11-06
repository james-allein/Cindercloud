package cloud.cinder.cindercloud.address.controller.vo;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigInteger;

@AllArgsConstructor
@Data
public class AddressVO {

    private String code;
    private String balance;
    private BigInteger transactionCount;
    private Page<Transaction> transactions;
    private Page<Block> minedBlocks;

    public boolean isContract() {
        return (code != null && !code.equals("0x"));
    }
}
