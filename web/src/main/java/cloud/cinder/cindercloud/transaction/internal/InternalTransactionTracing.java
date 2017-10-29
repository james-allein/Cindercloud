package cloud.cinder.cindercloud.transaction.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public class InternalTransactionTracing {

    @Autowired
    private Web3j web3j;

    public void doCheck() {
    }

}
