package cloud.cinder.cindercloud.transaction.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select transaction from Transaction transaction where fromAddress like :address or toAddress like :address")
    Slice<Transaction> findByAddressFromOrTo(@Param("address") final String address, final Pageable pageable);

    Slice<Transaction> findAllByBlockHash(@Param("blockHash") final String blockHash, final Pageable pageable);

    @Query("select transaction from Transaction transaction order by blockHeight desc")
    Slice<Transaction> findAllOrOrderByBlockTimestampAsPage(Pageable pageable);

    @Query("select transaction from Transaction transaction order by blockHeight desc")
    List<Transaction> findAllOrOrderByBlockTimestampAsList(Pageable pageable);

    @Query("select transaction from Transaction transaction where hash LIKE %:searchKey% AND blockHash LIKE %:block% order by blockHeight desc")
    Slice<Transaction> find(@Param("searchKey") String searchKey, @Param("block") final String block, final Pageable pageable);

    @Query("select transaction from Transaction transaction where blockTimestamp >= :from order by blockHeight")
    Stream<Transaction> findAllTransactionsSince(@Param("from") final Date from);
}
