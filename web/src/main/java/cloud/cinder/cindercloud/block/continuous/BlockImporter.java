package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.continuous.model.BlockImportJob;
import cloud.cinder.cindercloud.block.continuous.repository.BlockImportJobRepository;
import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import cloud.cinder.cindercloud.block.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
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

    @Value("${cloud.cinder.ethereum.auto-import:false}")
    private boolean autoBlockImport;

    @PostConstruct
    public void listenToBlocks() {
        if (autoBlockImport) {
            web3j.blockObservable(false)
                    .map(EthBlock::getBlock)
                    .filter(Objects::nonNull)
                    .map(Block::asBlock)
                    .subscribe(block -> blockService.save(block));
        }
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
                .map(Block::asBlock)
                .forEach(block -> blockService.save(block));

        job.setActive(false);
        job.setEndTime(new Date());
        blockImportJobRepository.save(job);
    }

}
