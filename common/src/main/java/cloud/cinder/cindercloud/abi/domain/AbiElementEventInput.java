package cloud.cinder.cindercloud.abi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbiElementEventInput {
    private String name;
    private String type;
    private boolean indexed;
}
