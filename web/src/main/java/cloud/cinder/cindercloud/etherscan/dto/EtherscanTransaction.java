package cloud.cinder.cindercloud.etherscan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtherscanTransaction {

    private String hash;
    private String blockHash;
}
