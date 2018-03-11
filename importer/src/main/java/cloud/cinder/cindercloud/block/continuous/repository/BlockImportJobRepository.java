package cloud.cinder.cindercloud.block.continuous.repository;

import cloud.cinder.cindercloud.block.domain.BlockImportJob;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlockImportJobRepository extends JpaRepository<BlockImportJob, Long> {

    @Query("select job from BlockImportJob job where active = true and endTime != null")
    List<BlockImportJob> findAllActive();

    @Query("select job from BlockImportJob job where active = false and endTime = null")
    List<BlockImportJob> findAllNotEndedYet();
}
