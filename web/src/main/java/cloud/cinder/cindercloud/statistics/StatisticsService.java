package cloud.cinder.cindercloud.statistics;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.repository.BlockRepository;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class StatisticsService {

    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public double getAverageDifficulty(final TemporalAmount since) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        final Stream<Block> allSince = blockRepository.findAllBlocksSince(Date.from(dateSince.toInstant()));
        return allSince
                .map(Block::getDifficulty)
                .mapToDouble(BigInteger::doubleValue)
                .average().orElse(0);
    }

    @Transactional(readOnly = true)
    public double getAverageTransactions(final TemporalAmount since, final TemporalUnit per) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        final Stream<Transaction> allSince = transactionRepository.findAllTransactionsSince(Date.from(dateSince.toInstant()));
        return allSince.count() / since.get(per);
    }

    @Transactional(readOnly = true)
    public double getAverageBlockTime(final TemporalAmount since, final TemporalUnit per) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        Stream<Block> allSince = blockRepository.findAllBlocksSince(Date.from(dateSince.toInstant()));
        return since.get(per) / allSince.count();
    }
}
