package cloud.cinder.cindercloud.token.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.token.domain.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findBySlug(@Param("slug") final String slug);

    @Query("select t from Token t where lower(address) LIKE lower(:address)")
    Optional<Token> findByAddressLike(@Param("address") final String address);

}
