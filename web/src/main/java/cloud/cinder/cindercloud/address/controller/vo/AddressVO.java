package cloud.cinder.cindercloud.address.controller.vo;

import cloud.cinder.cindercloud.block.domain.Block;
import cloud.cinder.cindercloud.transaction.domain.Transaction;
import cloud.cinder.cindercloud.utils.domain.PrettyAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.math.BigInteger;

@AllArgsConstructor
@Data
public class AddressVO {

    private String code;
    private PrettyAmount balance;
    private BigInteger transactionCount;
    private Slice<Transaction> transactions;
    private Slice<Block> minedBlocks;

    public boolean isContract() {
        return (code != null && !code.equals("0x"));
    }
}
