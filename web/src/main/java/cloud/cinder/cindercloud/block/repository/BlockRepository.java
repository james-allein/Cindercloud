package cloud.cinder.cindercloud.block.repository;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("select block from Block block order by height desc")
    Page<Block> findAllOrderByHeightDesc(Pageable pageable);

    @Query("select block from Block block where hash like %:searchKey% or height LIKE %:searchKey% order by height desc")
    Page<Block> search(final String searchKey, final Pageable pageable);
}
