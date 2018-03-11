package cloud.cinder.cindercloud.transaction.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select transaction from Transaction transaction where fromAddress like :address or toAddress like :address")
    Page<Transaction> findByAddressFromOrTo(@Param("address") final String address, final Pageable pageable);

    Page<Transaction> findAllByBlockHash(@Param("blockHash") final String blockHash, final Pageable pageable);

    @Query("select transaction from Transaction transaction order by blockTimestamp desc")
    List<Transaction> findAllOrOrderByBlockTimestamp(Pageable pageable);

    @Query("select transaction from Transaction transaction where hash LIKE %:searchKey% AND blockHash LIKE %:block% order by blockTimestamp desc")
    Page<Transaction> find(@Param("searchKey") String searchKey, @Param("block") final String block, final Pageable pageable);

    @Query("select transaction from Transaction transaction where blockTimestamp >= :from order by blockHeight")
    Stream<Transaction> findAllTransactionsSince(@Param("from") final Date from);

    @Query("select transaction from Transaction transaction order by blockHeight desc")
    Slice<Transaction> getTransactionsOrderedByHeight(final Pageable pageable);

    @Query("select transaction from Transaction transaction where blockHeight < :blockHeight")
    Stream<Transaction> findAllTransactionsBefore(final BigInteger blockHeight);
}
