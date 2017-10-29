package cloud.cinder.cindercloud.token.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.token.model.Token;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findBySlug(@Param("slug") final String slug);
    Optional<Token> findByAddress(@Param("address") final String address);

}
