package cloud.cinder.cindercloud.waves.gateway;

import com.wavesplatform.wavesj.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WavesGateway {

    @Autowired
    private Node wavesNode;

    public Node node() {
        return wavesNode;
    }
}
