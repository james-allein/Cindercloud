package cloud.cinder.cindercloud.token.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.token.domain.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
