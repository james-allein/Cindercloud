package cloud.cinder.cindercloud.block.continuous;

import cloud.cinder.cindercloud.block.continuous.repository.BlockImportJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class BlockImportJobRunner {

    @Autowired
    private BlockImportJobRepository blockImportJobRepository;
    @Autowired
    private BlockImporter blockImporter;

    @Scheduled(fixedDelay = 10000)
    public void run() {
        Stream.concat(blockImportJobRepository.findAllActive().stream(), blockImportJobRepository.findAllNotEndedYet().stream())
                .forEach(x -> blockImporter.execute(x));
    }

}
