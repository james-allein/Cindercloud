package cloud.cinder.cindercloud.token.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.token.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
