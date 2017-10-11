package cloud.cinder.cindercloud.block.service;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import cloud.cinder.cindercloud.infrastructure.service.SqsQueueSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByHash;
import rx.Observable;

import java.math.BigInteger;
import java.util.Objects;

@Component
public class BlockService {

    @Autowired
    private Web3j web3j;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SqsQueueSender $;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public Block save(final Block block) {
        final Block savedBlock = blockRepository.save(block);
        try {
            web3j.ethGetBlockTransactionCountByHash(savedBlock.getHash())
                    .observable()
                    .filter(Objects::nonNull)
                    .map(EthGetBlockTransactionCountByHash::getTransactionCount)
                    .filter(Objects::nonNull)
                    .filter(txCount -> txCount.compareTo(BigInteger.ZERO) != 0)
                    .subscribe(txCount -> {
                        try {
                            $.send(objectMapper.writeValueAsString(savedBlock), "block_with_transactions_imported");
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    });
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return savedBlock;
    }

    @Transactional
    public Observable<Block> getBlock(final String hash) {
        return blockRepository.findOne(hash)
                .map(Observable::just)
                .orElse(
                        web3j.ethGetBlockByHash(hash, false)
                                .observable()
                                .map(EthBlock::getBlock)
                                .filter(Objects::nonNull)
                                .map(block -> Block.builder()
                                        .difficulty(block.getDifficulty())
                                        .difficultyTotal(block.getTotalDifficulty())
                                        .extraData(block.getExtraData())
                                        .hash(block.getHash())
                                        .mixHash(block.getMixHash())
                                        .gasLimit(block.getGasLimit())
                                        .gasUsed(block.getGasUsed())
                                        .minedBy(block.getMiner())
                                        .sha3Uncles(block.getSha3Uncles())
                                        .nonce(block.getNonceRaw() != null ? block.getNonce() : BigInteger.ZERO)
                                        .size(block.getSize())
                                        .timestamp(block.getTimestamp())
                                        .parentHash(block.getParentHash())
                                        .receiptsRoot(block.getReceiptsRoot())
                                        .height(block.getNumber())
                                        .build())
                                .map(this::save)
                );
    }

}
