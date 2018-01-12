package cloud.cinder.cindercloud.erc20.service;

import cloud.cinder.cindercloud.erc20.model.HumanStandardToken;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;

@Component
public class ERC20Service {

    private static final Credentials DUMMY = Credentials.create("0x0");

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
        return HumanStandardToken.load(token, web3j.web3j(), DUMMY, BigInteger.valueOf(0), BigInteger.valueOf(0));
    }
}
