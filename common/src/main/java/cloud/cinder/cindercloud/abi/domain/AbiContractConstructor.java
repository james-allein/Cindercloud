package cloud.cinder.cindercloud.abi.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class AbiContractConstructor extends AbiContractElement {
    private boolean payable;
    private String stateMutability;
    private List<AbiElementInput> inputs;
}
