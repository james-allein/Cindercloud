package cloud.cinder.cindercloud.token.listener;

import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.service.TokenService;
import cloud.cinder.cindercloud.token.service.TokenTransferService;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                        log.debug("starting importing historic data for {}", token.getName());
                        final EthLog ethLog = web3jGateway.web3j()
                                .ethGetLogs(contractEventsFilter(token, Optional.of(3289417L), Optional.empty())).send();
                        ethLog.getLogs()
                                .stream()
                                .parallel()
                                .forEach(x -> {
                                    final Log log = (Log) x.get();
                                    tokenTransferService.getEventParameters(TRANSFER_EVENT, log)
                                            .ifPresent(tokenTransferListener.submitTokenTransfer(log));
                                });
                        log.debug("ended importing historic data for {}", token.getName());
                    } catch (final Exception ex) {
                        log.debug("Problem trying to import history for token: {}", token.getName(), ex);
                    }
                });
    }

    private EthFilter contractEventsFilter(final Token token, Optional<Long> fromOptional, Optional<Long> toOptional) {
        DefaultBlockParameter from = fromOptional.map(x -> (DefaultBlockParameter) new DefaultBlockParameterNumber(x)).orElse(DefaultBlockParameterName.EARLIEST);
        DefaultBlockParameter to = toOptional.map(x -> (DefaultBlockParameter) new DefaultBlockParameterNumber(x)).orElse(DefaultBlockParameterName.LATEST);
        EthFilter ethFilter = new EthFilter(from, to, token.getAddress());
        ethFilter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return ethFilter;
    }
}
