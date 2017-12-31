package cloud.cinder.cindercloud.address.service;

import cloud.cinder.cindercloud.address.model.SpecialAddress;
import cloud.cinder.cindercloud.address.repository.SpecialAddressRepository;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import rx.Observable;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class AddressService {

    @Autowired
    private Web3jGateway web3j;

    @Autowired
    private SpecialAddressRepository specialAddressRepository;

    public Observable<String> getCode(final String hash) {
        return web3j.web3j().ethGetCode(hash, DefaultBlockParameterName.LATEST).observable().map(EthGetCode::getCode);
    }

    public Observable<BigInteger> getBalance(final String address) {
        return web3j.web3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).observable().map(
                EthGetBalance::getBalance);
    }

    public Observable<BigInteger> getTransactionCount(final String address) {
        return web3j.web3j().ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).observable()
                .map(EthGetTransactionCount::getTransactionCount);
    }

    public Optional<SpecialAddress> findByAddress(final String address) {
        return specialAddressRepository.findByAddress(address);
    }
}
