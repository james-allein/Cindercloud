package cloud.cinder.cindercloud.abi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AbiContract {

    private List<AbiContractElement> elements;

}
