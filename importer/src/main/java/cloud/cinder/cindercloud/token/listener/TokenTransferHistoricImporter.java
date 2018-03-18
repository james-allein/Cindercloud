package cloud.cinder.cindercloud.token.listener;

import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.service.TokenService;
import cloud.cinder.cindercloud.token.service.TokenTransferService;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import java.util.Optional;

import static cloud.cinder.cindercloud.token.listener.TokenTransferListener.TRANSFER_EVENT;

@Component
@ConditionalOnProperty(name = "cloud.cinder.ethereum.token-transfer-import", havingValue = "true")
public class TokenTransferHistoricImporter {

    private final Web3jGateway web3jGateway;
    private final TokenTransferService tokenTransferService;
    private final TokenService tokenService;
    private final TokenTransferListener tokenTransferListener;

    public TokenTransferHistoricImporter(final Web3jGateway web3jGateway, final TokenTransferService tokenTransferService, final TokenService tokenService, final TokenTransferListener tokenTransferListener) {
        this.web3jGateway = web3jGateway;
        this.tokenTransferService = tokenTransferService;
        this.tokenService = tokenService;
        this.tokenTransferListener = tokenTransferListener;
    }

    @Scheduled(fixedDelay = (60000 * 60 * 24))
    public void importEverything() {
        tokenService.findAll()
                .forEach(token -> {
                    try {
                        EthLog ethLog = web3jGateway.web3j()
                                .ethGetLogs(contractEventsFilter(token, Optional.empty(), Optional.empty())).send();
                        ethLog.getLogs()
                                .forEach(x -> {
                                    Log log = (Log) x.get();
                                    tokenTransferService.getEventParameters(TRANSFER_EVENT, log)
                                            .ifPresent(tokenTransferListener.submitTokenTransfer(log));
                                });
                    } catch (final Exception ex) {
                        System.out.println(ex);
                    }
                });
    }

    private EthFilter contractEventsFilter(final Token token, Optional<Long> fromOptional, Optional<Long> toOptional) {
        DefaultBlockParameter from = fromOptional.map(x -> (DefaultBlockParameter) new DefaultBlockParameterNumber(x)).orElse(DefaultBlockParameterName.EARLIEST);
        DefaultBlockParameter to = toOptional.map(x -> (DefaultBlockParameter) new DefaultBlockParameterNumber(x)).orElse(DefaultBlockParameterName.LATEST);
        EthFilter ethFilter = new EthFilter(from, to, token.getAddress());
        ethFilter.addOptionalTopics(EventEncoder.encode(TRANSFER_EVENT));
        return ethFilter;
    }
}
