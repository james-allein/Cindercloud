package cloud.cinder.cindercloud.abi.domain;

import lombok.Getter;

@Getter
public class AbiContractFallback extends AbiContractElement {

    private String stateMutability;
    private boolean payable;

}
