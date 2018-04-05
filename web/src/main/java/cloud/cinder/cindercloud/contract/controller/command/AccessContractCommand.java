package cloud.cinder.cindercloud.contract.controller.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessContractCommand {

    private String address;
    private String abi;
}
