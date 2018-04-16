package cloud.cinder.cindercloud.abi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AbiContractEvent extends AbiContractElement {

    private boolean anonymous;
    private String name;
    private List<AbiElementEventInput> inputs;

}
