package cloud.cinder.cindercloud.token.service;

import cloud.cinder.cindercloud.token.model.Token;
import cloud.cinder.cindercloud.token.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public Optional<Token> findByAddress(final String address) {
        return tokenRepository.findByAddressLike(address);
    }

    public List<Token> findAll() {
        return tokenRepository.findAll();
    }
}
