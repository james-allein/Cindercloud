package cloud.cinder.cindercloud.token.listener;

import cloud.cinder.cindercloud.token.listener.model.UserTokenRequest;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "token-transfer-queue-import", havingValue = "true")
@Slf4j
public class UserTokenImportListener {

    private ObjectMapper objectMapper;
    private TokenTransferHistoricImporter tokenTransferHistoricImporter;
    private final Meter blockImportMeter;

    public UserTokenImportListener(final ObjectMapper objectMapper,
                                   final MetricRegistry metricRegistry,
                                   final TokenTransferHistoricImporter tokenTransferHistoricImporter) {
        this.objectMapper = objectMapper;
        this.tokenTransferHistoricImporter = tokenTransferHistoricImporter;

        this.blockImportMeter = metricRegistry.meter("user_token_imports");
    }

    public void receiveMessage(final String userTokenImportRequestAsString) {
        try {
            log.debug("Fetched from queue: {}", userTokenImportRequestAsString);
            final UserTokenRequest userTokenRequest = objectMapper.readValue(userTokenImportRequestAsString, UserTokenRequest.class);
            tokenTransferHistoricImporter.importForUserTokens(userTokenRequest);
        } catch (final Exception ex) {
            log.error("Error trying to receive message", ex);
            throw new IllegalArgumentException("Unable to process");
        } finally {
            blockImportMeter.mark();
        }
    }
}
