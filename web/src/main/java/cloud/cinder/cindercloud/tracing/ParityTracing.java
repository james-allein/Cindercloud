package cloud.cinder.cindercloud.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;

import java.util.Arrays;

@Component
public class ParityTracing {

    protected static final long ID = 1;

    @Autowired
    @Qualifier("archive")
    private Web3jService web3jService;

    public Request<?, ParityTracingResponse> replayTransaction(
            final String txHash) {
        return new Request<>(
                "trace_replayTransaction",
                Arrays.asList(txHash, Arrays.asList("trace")),
                ID,
                web3jService,
                ParityTracingResponse.class);
    }


}
