package cloud.cinder.cindercloud.token.service;

import cloud.cinder.cindercloud.erc20.domain.HumanStandardToken;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
@Slf4j
public class ERC20Service {

    private static final Credentials DUMMY = Credentials.create("0x0");

    @Autowired
    private Web3jGateway web3j;

    @Cacheable(cacheNames = "wallets.tokens.amount", key = "#address+'-'+#token")
    public BigDecimal balanceOf(final String address, final String token) {
        final HumanStandardToken erc20 = getERC20(token);
        try {
            final BigInteger decimals = erc20.decimals().send();
            final BigDecimal rawBalance = new BigDecimal(erc20.balanceOf(address).send());
            final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals.intValue());
            return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN);
        } catch (final Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @CacheEvict(cacheNames = "wallets.tokens.amount", key = "#address+'-'+#token")
    public void evictBalanceOf(final String address, final String token) {
    }

    private HumanStandardToken getERC20(final String token) {
        return HumanStandardToken.load(token, web3j.web3j(), DUMMY, BigInteger.valueOf(0), BigInteger.valueOf(0));
    }

}
