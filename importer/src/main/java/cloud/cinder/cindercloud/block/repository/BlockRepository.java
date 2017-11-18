package cloud.cinder.cindercloud.block.repository;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("select block from Block block where uncle = false and hash LIKE :hash")
    Optional<Block> findBlock(@Param("hash") final String hash);
}