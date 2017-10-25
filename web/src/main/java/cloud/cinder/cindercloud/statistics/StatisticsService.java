package cloud.cinder.cindercloud.statistics;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class StatisticsService {

    @Autowired
    private BlockRepository blockRepository;

    public double getAverageDifficulty(final TemporalAmount since) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        final Stream<Block> allSince = blockRepository.findAllBlocksSince(Date.from(dateSince.toInstant()));
        return allSince
                .map(Block::getDifficulty)
                .mapToDouble(BigInteger::doubleValue)
                .average().orElse(0);
    }

//    public double getAverageTransactions(final TemporalAmount since) {
//
//    }
//
//    public double getAverageBlockTime(final TemporalAmount since) {
//
//    }
}
