package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.continuous.model.BlockImportJob;
import cloud.cinder.cindercloud.block.continuous.repository.BlockImportJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.ethereum.historic-block-import", havingValue = "true")
@EnableScheduling
public class HistoricBlockImportJobRunner {

    @Autowired
    private BlockImportJobRepository blockImportJobRepository;
    @Autowired
    private BlockImporter blockImporter;

    @Scheduled(fixedDelay = 10000)
    public void run() {
        log.info("Starting Block Import Job Runner");
        final List<BlockImportJob> allActive = blockImportJobRepository.findAllActive();
        final List<BlockImportJob> allNotEndedYet = blockImportJobRepository.findAllNotEndedYet();
        List<BlockImportJob> union = ListUtils.union(allActive, allNotEndedYet);
        if (union.size() > 0) {
            blockImporter.execute(union.get(0));
        }
        log.info("Done with block runner");
    }

}
