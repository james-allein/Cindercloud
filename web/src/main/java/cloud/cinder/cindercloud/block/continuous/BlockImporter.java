package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;

@Component
public class BlockImporter {

    @Autowired
    private Web3j web3j;

    @Autowired
    private BlockRepository blockRepository;

    public void run() {
        web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(
                DefaultBlockParameterName.EARLIEST,
                false).map(EthBlock::getBlock)
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
                .filter(block -> !blockRepository.findOne(block.getHash()).isPresent())
                .map(block -> {
                    System.out.println(block);
                    return block;
                })
                .forEach(block -> blockRepository.save(block));
    }

}
