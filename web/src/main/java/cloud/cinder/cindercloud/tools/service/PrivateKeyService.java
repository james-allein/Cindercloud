package cloud.cinder.cindercloud.tools.service;

import cloud.cinder.cindercloud.credentials.CredentialService;
import cloud.cinder.cindercloud.tools.service.dto.PrivateKeyCheckResult;
import cloud.cinder.cindercloud.utils.WeiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PrivateKeyService {

    @Autowired
    private Web3j web3j;
    @Autowired
    private CredentialService credentialService;

    public List<PrivateKeyCheckResult> check(final List<String> privateKeys) {
        return privateKeys
                .stream()
                .peek(x -> credentialService.saveLeakedCredential(x))
                .map(x -> {
                    try {
                        final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(x.trim()));
                        final String address = prettifyAddress(Keys.getAddress(keypair));
                        final BigInteger balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).observable().toBlocking().first().getBalance();
                        final BigInteger txCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).observable().toBlocking().first().getTransactionCount();
                        return PrivateKeyCheckResult.builder()
                                .address(address)
                                .balance(WeiUtils.format(balance).toString())
                                .privateKey(x)
                                .txCount(txCount.toString())
                                .rawTxCount(txCount)
                                .rawBalance(balance)
                                .build();
                    } catch (final Exception exception) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String prettifyAddress(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }
}
