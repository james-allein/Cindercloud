package cloud.cinder.cindercloud.block.rest;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.continuous.TransactionImporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/import/block/transactions")
@Slf4j
@Profile("aws")
public class ImportBlockTransactionsController {

    @Autowired
    private TransactionImporter transactionImporter;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> importTransactions(@RequestBody final String blockAsString) {
        try {
            log.debug("Fetched from queue: {}", blockAsString);
            final Block convertedBlock = objectMapper.readValue(blockAsString, Block.class);
            transactionImporter.importTransactions(convertedBlock);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (final Exception ex) {
            log.error("Error trying to receive message", ex);
            throw new IllegalArgumentException("Unable to process");
        }
    }
}
