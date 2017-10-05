package cloud.cinder.cindercloud.block.repository;

import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, String> {
}
