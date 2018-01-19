package cloud.cinder.cindercloud.transaction.continuous;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class TransactionImporter {

    @Autowired
    private Web3jGateway web3j;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    @Transactional
    public void importTransactions(final Block convertedBlock) {
        try {
            web3j.web3j().ethGetBlockByHash(convertedBlock.getHash(), true)
                    .observable()
                    .filter(bk -> bk.getBlock() != null)
                    .flatMapIterable(bk -> bk.getBlock().getTransactions())
                    .filter(tx -> tx.get() != null && tx.get() instanceof EthBlock.TransactionObject && ((EthBlock.TransactionObject) tx.get()).get() != null)
                    .map(tx -> ((EthBlock.TransactionObject) tx.get()).get())
                    .map(tx -> {
                        log.trace("importing transaction {}", tx.getHash());
                        return Transaction.builder()
                                .blockHash(tx.getBlockHash())
                                .blockHeight(convertedBlock.getHeight())
                                .fromAddress(tx.getFrom())
                                .gas(tx.getGas())
                                .hash(tx.getHash())
                                .input(tx.getInput())
                                .toAddress(tx.getTo())
                                .value(tx.getValue())
                                .blockTimestamp(Date.from(LocalDateTime.ofEpochSecond(convertedBlock.getTimestamp().longValue(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                                .gasPrice(tx.getGasPrice())
                                .creates(tx.getCreates())
                                .s(tx.getS())
                                .r(tx.getR())
                                .v(tx.getV())
                                .nonce(tx.getNonce())
                                .transactionIndex(tx.getTransactionIndex())
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .forEach(transactionService::saveIfNotExists);
        } catch (final Exception e) {
            log.error("Error trying to import transactions from block", e);
        }
    }
}