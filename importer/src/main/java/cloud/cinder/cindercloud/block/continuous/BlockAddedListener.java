package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.continuous.TransactionImporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.ethereum.sqs-transaction-import", havingValue = "true")
public class BlockAddedListener {

    @Autowired
    private TransactionImporter transactionImporter;
    @Autowired
    private ObjectMapper objectMapper;

    public void receiveMessage(final String blockAsString) {
        try {
            log.debug("Fetched from queue: {}", blockAsString);
            final Block convertedBlock = objectMapper.readValue(blockAsString, Block.class);
            transactionImporter.importTransactions(convertedBlock);
        } catch (final Exception ex) {
            log.error("Error trying to receive message", ex);
            throw new IllegalArgumentException("Unable to process");
        }
    }
}