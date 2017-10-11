package cloud.cinder.cindercloud.transaction.continuous;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class TransactionImporter {

    @Autowired
    private Web3j web3j;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    @SqsListener(value = "cindercloud_kovan", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void importTransactions(final String blockAsString ,
                                   @Header("event_type") String eventType) {

        if ("block_with_transactions_imported".equals(eventType)) {
            try {
                Block convertedBlock = objectMapper.readValue(blockAsString, Block.class);
                web3j.ethGetBlockByHash(convertedBlock.getHash(), true)
                        .observable()
                        .filter(bk -> bk.getBlock() != null)
                        .flatMapIterable(bk -> bk.getBlock().getTransactions())
                        .filter(tx -> tx.get() != null && tx.get() instanceof EthBlock.TransactionObject && ((EthBlock.TransactionObject) tx.get()).get() != null)
                        .map(tx -> ((EthBlock.TransactionObject) tx.get()).get())
                        .filter(tx -> !transactionRepository.exists(tx.getHash()))
                        .map(tx -> {
                            System.out.println(tx.getHash());
                            return Transaction.builder()
                                    .blockHash(tx.getBlockHash())
                                    .fromAddress(tx.getFrom())
                                    .gas(tx.getGas())
                                    .hash(tx.getHash())
                                    .input(tx.getInput())
                                    .toAddress(tx.getTo())
                                    .value(tx.getValue())
                                    .gasPrice(tx.getGasPrice())
                                    .creates(tx.getCreates())
                                    .s(tx.getS())
                                    .r(tx.getR())
                                    .v(tx.getV())
                                    .nonce(tx.getNonce())
                                    .transactionIndex(tx.getTransactionIndex())
                                    .build();
                        }).forEach(transactionService::save);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("not a valid messagetype");
        }

    }

}
