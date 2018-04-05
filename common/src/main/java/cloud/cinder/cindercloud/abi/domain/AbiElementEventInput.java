package cloud.cinder.cindercloud.abi.domain;

import lombok.Getter;

@Getter
public class AbiElementEventInput {
    private String name;
    private String type;
    private boolean indexed;
}
