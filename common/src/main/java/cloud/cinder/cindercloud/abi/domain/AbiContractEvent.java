package cloud.cinder.cindercloud.abi.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class AbiContractEvent extends AbiContractElement {

    private boolean anonymous;
    private String name;
    private List<AbiElementEventInput> inputs;

}
