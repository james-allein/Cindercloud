package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.continuous.model.BlockImportJob;
import cloud.cinder.cindercloud.block.continuous.repository.BlockImportJobRepository;
import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import cloud.cinder.cindercloud.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import java.util.stream.LongStream;

@Component
public class BlockImporter {

    @Autowired
    private Web3j web3j;
    @Autowired
    private BlockService blockService;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private BlockImportJobRepository blockImportJobRepository;

    @PostConstruct
    public void listenToBlocks() {
        web3j.blockObservable(false)
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
                        .txCount((long) block.getTransactions().size())
                        .timestamp(block.getTimestamp())
                        .parentHash(block.getParentHash())
                        .receiptsRoot(block.getReceiptsRoot())
                        .height(block.getNumber())
                        .build())
                .subscribe(block -> blockService.save(block));
    }

    public void execute(final BlockImportJob job) {
        job.setActive(true);
        job.setStartTime(new Date());
        blockImportJobRepository.save(job);

        LongStream.rangeClosed(job.getFromBlock(), job.getToBlock())
                .mapToObj(blocknr -> web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blocknr), true)
                        .observable().toBlocking().first())
                .filter(Objects::nonNull)
                .map(EthBlock::getBlock)
                .filter(Objects::nonNull)
                .filter(block -> !blockRepository.findOne(block.getHash()).isPresent())
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
                .forEach(block -> blockService.save(block));

        job.setActive(false);
        job.setEndTime(new Date());
        blockImportJobRepository.save(job);
    }

}
