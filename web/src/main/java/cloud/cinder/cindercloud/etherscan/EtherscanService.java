package cloud.cinder.cindercloud.etherscan;

import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.etherscan.dto.EtherscanResponse;
import cloud.cinder.cindercloud.etherscan.dto.EtherscanTransaction;
import cloud.cinder.cindercloud.login.domain.LoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EtherscanService {

    @Autowired
    private EtherscanClient etherscanClient;
    @Autowired
    private BlockService blockService;
    @Value("${cloud.cinder.etherscan-enabled:false}")
    private boolean enabled;

    @Async
    public void importByAddress(final String address) {
        if (enabled) {
            try {
                final EtherscanResponse transactions = etherscanClient.transactions(address);
                if (transactions.isSuccess() && transactions.getResult() != null) {
                    transactions.getResult()
                            .stream()
                            .map(EtherscanTransaction::getBlockHash)
                            .forEach(x -> {
                                try {
                                    log.trace("importing block {}", x);
                                    blockService.getBlock(x).subscribe();
                                } catch (final Exception ex) {
                                    log.debug("unable to find block {}", x);
                                }
                            });
                }
            } catch (final Exception ex) {
                log.debug("Unable to fetch transactions from etherscan for address {}", address);
            }
        }
    }

    @Async
    @EventListener
    public void etherscanAfterImport(final LoginEvent loginEvent) {
        importByAddress(loginEvent.getWallet());
    }
}
