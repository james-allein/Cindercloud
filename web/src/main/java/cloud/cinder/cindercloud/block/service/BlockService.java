package cloud.cinder.cindercloud.block.service;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import cloud.cinder.cindercloud.infrastructure.service.SqsQueueSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByHash;
import rx.Observable;

import java.math.BigInteger;
import java.util.Objects;

@Component
@Slf4j
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
            propagateTransactions(savedBlock);
        } catch (final Exception ex) {
            log.error("Error while trying to get transactions from block", ex);
        }
        importUncles(block);
        return savedBlock;
    }

    private void propagateTransactions(final Block savedBlock) {
        web3j.ethGetBlockTransactionCountByHash(savedBlock.getHash())
                .observable()
                .filter(Objects::nonNull)
                .map(EthGetBlockTransactionCountByHash::getTransactionCount)
                .filter(Objects::nonNull)
                .filter(txCount -> txCount.compareTo(BigInteger.ZERO) != 0)
                .subscribe(txCount -> {
                    try {
                        $.send(objectMapper.writeValueAsString(savedBlock), "block_with_transactions_imported");
                    } catch (final Exception ex) {
                        log.error("Problem while trying to send block with transactions to sqs", ex);
                    }
                });
    }

    private void importUncles(final Block block) {
        web3j.ethGetBlockByHash(block.getHash(), false)
                .observable()
                .filter(Objects::nonNull)
                .map(EthBlock::getBlock)
                .filter(Objects::nonNull)
                .flatMapIterable(EthBlock.Block::getUncles)
                .forEach(x -> importByHash(x, true));
    }

    @Transactional
    public void importByHash(final String hash, final boolean uncle) {
        if (wasWronglySavedAsNormalBlock(hash, uncle)) {
            blockRepository.delete(hash);
        }

        web3j.ethGetBlockByHash(hash, false)
                .observable()
                .map(EthBlock::getBlock)
                .filter(Objects::nonNull)
                .map(block -> {
                    if (uncle) {
                        return Block.asBlock(block);
                    } else {
                        return Block.asUncle(block);
                    }
                })
                .map(this::save)
                .toBlocking().single();
    }

    private boolean wasWronglySavedAsNormalBlock(final String hash, final boolean uncle) {
        return uncle && blockRepository.findBlock(hash).isPresent();
    }

    public Observable<Block> getUncle(final String hash) {
        return blockRepository.findUncle(hash)
                .map(Observable::just).orElse(Observable.empty());
    }

    @Transactional
    public Observable<Block> getBlock(final String hash) {
        return blockRepository.findBlock(hash)
                .map(Observable::just)
                .orElse(
                        web3j.ethGetBlockByHash(hash, false)
                                .observable()
                                .map(EthBlock::getBlock)
                                .filter(Objects::nonNull)
                                .map(Block::asBlock)
                                .map(this::save)
                );
    }

    @Transactional(readOnly = true)
    public Page<Block> getLastImportedBlock() {
        return blockRepository.findAllBlocksOrderByHeightDesc(new PageRequest(0, 1));
    }

    @Transactional(readOnly = true)
    public Page<Block> getLastBlocks(final Pageable pageable) {
        return blockRepository.findAllBlocksOrderByHeightDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Block> getLastUncles(final Pageable pageable) {
        return blockRepository.findAllUnclesOrderByHeightDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Block> searchBlocks(final String searchKey, final Pageable pageable) {
        return blockRepository.searchBlocks(searchKey, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Block> searchUncles(final String searchKey, final Pageable pageable) {
        return blockRepository.searchUncles(searchKey, pageable);
    }
}
