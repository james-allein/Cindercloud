package cloud.cinder.cindercloud.abi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AbiContractFunction extends AbiContractElement {

    private String name;
    private boolean payable;
    private String stateMutability;
    private boolean constant;
    private List<AbiElementInput> inputs;
    private List<AbiElementOutput> outputs;

}
