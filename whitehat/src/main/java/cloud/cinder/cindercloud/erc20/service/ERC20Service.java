package cloud.cinder.cindercloud.erc20.service;

import cloud.cinder.cindercloud.web3j.Web3jGateway;
import cloud.cinder.ethereum.token.domain.HumanStandardToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class ERC20Service {

    @Autowired
    private Web3jGateway web3j;

    public BigInteger rawBalanceOf(final String address, final String token) {
        final HumanStandardToken erc20 = getERC20(token);
        try {
            return erc20.balanceOf(address).send();
        } catch (final Exception e) {
            return BigInteger.ZERO;
        }
    }

    private HumanStandardToken getERC20(final String token) {
        return HumanStandardToken.load(token, web3j.web3j());
    }
}
