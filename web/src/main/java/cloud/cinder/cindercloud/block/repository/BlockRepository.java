package cloud.cinder.cindercloud.block.repository;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("select block from Block block where uncle = false order by height desc")
    Page<Block> findAllBlocksOrderByHeightDesc(Pageable pageable);

    @Query("select block from Block block where uncle = false order by height desc")
    Page<Block> findAllUnclesOrderByHeightDesc(Pageable pageable);

    @Query("select block from Block block where (hash like %:searchKey% or height LIKE %:searchKey%) and uncle = false order by height desc")
    Page<Block> searchBlocks(final String searchKey, final Pageable pageable);

    @Query("select block from Block block where (hash like %:searchKey% or height LIKE %:searchKey%) and uncle = true order by height desc")
    Page<Block> searchUncles(final String searchKey, final Pageable pageable);

    @Query("select block from Block block where timestampDateTime >= :since and uncle = false")
    Stream<Block> findAllBlocksSince(@Param("since") final Date date);

    @Query("select block from Block block where uncle = true and hash = :hash")
    Optional<Block> findUncle(@Param("hash") final String hash);

    @Query("select block from Block block where uncle = false and hash LIKE :hash")
    Optional<Block> findBlock(@Param("hash") final String hash);

    @Query("select block from Block block where minedBy LIKE :address")
    Page<Block> findByMiner(@Param("address") final String address, final Pageable pageable);
}
