package cloud.cinder.cindercloud.token.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.token.domain.TokenTransfer;

public interface TokenTransferRepository extends JpaRepository<TokenTransfer, Long> {
}
