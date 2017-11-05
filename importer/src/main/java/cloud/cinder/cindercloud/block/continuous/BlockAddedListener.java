package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.continuous.TransactionImporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.ethereum.sqs-transaction-import", havingValue = "true")
public class BlockAddedListener {

    @Autowired
    private TransactionImporter transactionImporter;
    @Autowired
    private ObjectMapper objectMapper;

    @SqsListener(value = "${cloud.cinder.sqs.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void importTransactions(final String blockAsString,
                                   @Header("event_type") String eventType) {
        if ("block_with_transactions_imported".equals(eventType)) {
            try {
                log.debug("Fetched from queue: {}", blockAsString);
                final Block convertedBlock = objectMapper.readValue(blockAsString, Block.class);
                transactionImporter.importTransactions(convertedBlock);
            } catch (final Exception ex) {
                log.error("Error trying to receive message", ex);
            }
        }
    }
}