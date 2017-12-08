package cloud.cinder.cindercloud.sweeping;

import cloud.cinder.cindercloud.mail.MailService;
import cloud.cinder.cindercloud.utils.WeiUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import rx.functions.Action1;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Objects;

@Component
@Slf4j
public class Sweeper {

    private static final BigInteger ETHER_TRANSACTION_GAS_LIMIT = BigInteger.valueOf(21000L);


    @Autowired
    private Web3j web3j;
    @Autowired
    private MailService mailService;

    @Value("${cloud.cinder.whitehat.address}")
    private String whitehatAddress;

    @Value("${cloud.cinder.whitehat.gasPrice}")
    private Long gasPrice;

    private BigInteger GAS_PRICE;
    private BigInteger GAS_COST;


    @PostConstruct
    public void init() {
        this.GAS_PRICE = BigInteger.valueOf(gasPrice);
        this.GAS_COST = this.GAS_PRICE.multiply(ETHER_TRANSACTION_GAS_LIMIT);
    }


    public void sweep(final String privateKey) {
        try {
            final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.trim()));
            final String address = Keys.getAddress(keypair);

            web3j.ethGetBalance(prettify(address), DefaultBlockParameterName.LATEST).observable()
                    .filter(Objects::nonNull)
                    .subscribe(balanceFetched(keypair));
        } catch (final Exception ex) {
            log.error("something went wrong while trying sweep {}", privateKey);
        }
    }

    private Action1<EthGetBalance> balanceFetched(final ECKeyPair keyPair) {
        return balance -> {
            if (balance.getBalance().longValue() != 0L) {

                //if balance is more than gasCost
                if (balance.getBalance().compareTo(GAS_COST) >= 0) {
                    log.debug("[Sweeper] {} has a balance of about {}", Keys.getAddress(keyPair), WeiUtils.format(balance.getBalance()));

                    final EthGetTransactionCount transactionCount = calculateNonce(keyPair);

                    if (transactionCount != null) {
                        final RawTransaction etherTransaction = RawTransaction.createEtherTransaction(
                                transactionCount.getTransactionCount(),
                                GAS_PRICE,
                                ETHER_TRANSACTION_GAS_LIMIT,
                                whitehatAddress,
                                balance.getBalance().subtract(GAS_COST)
                        );


                        final byte[] signedMessage = sign(keyPair, etherTransaction);
                        final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                        try {
                            EthSendTransaction send = web3j.ethSendRawTransaction(signedMessageAsHex).sendAsync().get();
                            log.debug("txHash: {}", send.getTransactionHash());
                            if (send.getTransactionHash() != null) {
                                mailService.send("Saved funds from compromised wallet!", "Hi Admin,\nWe just saved " + WeiUtils.format(balance.getBalance()).toString() + " from a compromised wallet[" + prettify(Keys.getAddress(keyPair) + "].\nKind regards,\nCindercloud"));
                            }
                        } catch (final Exception ex) {
                            log.error("Error sending transaction (io)");
                        }
                    } else {
                        log.error("Noncecount returned an error for {}", Keys.getAddress(keyPair));
                    }
                }
            }
        };
    }

    private byte[] sign(final ECKeyPair keyPair, final RawTransaction etherTransaction) {
        return TransactionEncoder.signMessage(etherTransaction, Credentials.create(keyPair));
    }

    private EthGetTransactionCount calculateNonce(final ECKeyPair keyPair) {
        return web3j.ethGetTransactionCount(
                prettify(Keys.getAddress(keyPair)),
                DefaultBlockParameterName.LATEST
        ).observable().toBlocking().first();
    }

    private String prettify(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }

}
