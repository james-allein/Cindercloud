package cloud.cinder.cindercloud.address.controller.vo;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.utils.dto.PrettyAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
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
