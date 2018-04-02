package cloud.cinder.cindercloud.token.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.token.domain.TokenTransfer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenTransferRepository extends JpaRepository<TokenTransfer, Long> {

    @Query("select transfer from TokenTransfer transfer where fromAddress LIKE :address or toAddress LIKE :address order by blockTimestamp DESC")
    List<TokenTransfer> findByFromOrTo(@Param("address") final String address, final Pageable pageable);

    @Query("select count(tt) > 0 from TokenTransfer tt where fromAddress LIKE :address or toAddress LIKE :address")
    boolean existsByFromOrTo(@Param("address") final String address);
}
