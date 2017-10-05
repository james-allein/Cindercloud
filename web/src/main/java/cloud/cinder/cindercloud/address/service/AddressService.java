package cloud.cinder.cindercloud.address.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import rx.Observable;

import java.math.BigInteger;

@Component
public class AddressService {

    @Autowired
    private Web3j web3j;

    public Observable<BigInteger> getBalance(final String address) {
        return web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).observable().map(
                EthGetBalance::getBalance);
    }

    public Observable<BigInteger> getTransactionCount(final String address) {
        return web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).observable()
                .map(EthGetTransactionCount::getTransactionCount);
    }
}
