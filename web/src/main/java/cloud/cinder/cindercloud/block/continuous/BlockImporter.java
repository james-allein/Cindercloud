package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.continuous.model.BlockImportJob;
import cloud.cinder.cindercloud.block.continuous.repository.BlockImportJobRepository;
import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import cloud.cinder.cindercloud.block.service.BlockService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            log.debug("importing live-blocks");
            web3j.blockObservable(false)
                    .map(EthBlock::getBlock)
                    .filter(Objects::nonNull)
                    .map(Block::asBlock)
                    .subscribe(block -> {
                        log.debug("received live block");
                        blockService.save(block);
                    });
        }
    }

    public void execute(final BlockImportJob job) {
        job.setActive(true);
        job.setStartTime(new Date());
        blockImportJobRepository.save(job);

        for (long i = job.getFromBlock() ;  i <= job.getToBlock(); i++) {
            log.debug("Historic Import: trying to import block: {}", i);
            final EthBlock ethBlock = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(i), false)
                    .observable().toBlocking().firstOrDefault(null);
            if (ethBlock != null && ethBlock.getBlock() != null && !blockRepository.findOne(ethBlock.getBlock().getHash()).isPresent()) {
                blockService.save(Block.asBlock(ethBlock.getBlock()));
            }
        }

        job.setActive(false);
        job.setEndTime(new Date());
        blockImportJobRepository.save(job);
    }
}
