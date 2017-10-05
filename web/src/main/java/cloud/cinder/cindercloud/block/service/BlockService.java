package cloud.cinder.cindercloud.block.service;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Observable;

@Component
public class BlockService {

    @Autowired
    private Web3j web3j;
    @Autowired
    private BlockRepository blockRepository;

    public Observable<Block> getBlock(final String hash) {
        return blockRepository.findOne(hash)
                .map(Observable::just)
                .orElse(
                        web3j.ethGetBlockByHash(hash, false)
                                .observable()
                                .map(EthBlock::getBlock)
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
                                        .nonce(block.getNonce())
                                        .size(block.getSize())
                                        .timestamp(block.getTimestamp())
                                        .parentHash(block.getParentHash())
                                        .receiptsRoot(block.getReceiptsRoot())
                                        .height(block.getNumber())
                                        .build())
                                .map(block -> blockRepository.save(block))
                );
    }

}
