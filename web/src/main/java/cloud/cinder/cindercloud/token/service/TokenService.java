package cloud.cinder.cindercloud.token.service;

import cloud.cinder.cindercloud.token.model.Token;
import cloud.cinder.cindercloud.token.repository.TokenRepository;
import cloud.cinder.cindercloud.token.solidity.ERC20;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private Web3j web3j;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public Optional<Token> getTokenByName(final String name) {
        return tokenRepository.findBySlug(name);
    }

    @Transactional(readOnly = true)
    public Optional<Token> getTokenByAddress(final String addressHash) {
        return tokenRepository.findByAddress(addressHash);
    }

    public void readTokenFromBlockchain(final String address) throws IOException {
        final ERC20 erc20 = new ERC20(address, web3j, BigInteger.ZERO, BigInteger.ZERO);
        System.out.println(erc20.version().toBlocking().first());
    }

}
