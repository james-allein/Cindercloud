package cloud.cinder.cindercloud.wallet.service;

import cloud.cinder.cindercloud.wallet.controller.command.ConfirmEtherTransactionCommand;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigInteger;

@Service
@Slf4j
public class Web3TransactionService {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private Web3j web3j;

    public String submitTransaction(final ConfirmEtherTransactionCommand etherTransactionCommand) {
        final String address = authenticationService.getAddress();
        final BigInteger balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .observable()
                .toBlocking()
                .first()
                .getBalance();

        if (hasEnoughBalance(balance, etherTransactionCommand)) {
            final EthGetTransactionCount ethGetTransactionCount = calculateNonce(address);
            if (ethGetTransactionCount != null) {
                final RawTransaction etherTransaction = generateTransaction(ethGetTransactionCount, etherTransactionCommand);

                final byte[] signedMessage = sign(etherTransaction);
                final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                try {
                    final EthSendTransaction send = web3j.ethSendRawTransaction(signedMessageAsHex).sendAsync().get();
                    log.debug("txHash: {}", send.getTransactionHash());
                    if (send.getTransactionHash() != null) {
                        return send.getTransactionHash();
                    } else {
                        log.error(send.getError().getMessage());
                        throw new IllegalArgumentException(send.getError().getMessage());
                    }
                } catch (final Exception ex) {
                    //TODO: handle "already imported" transactions
                    throw new IllegalArgumentException("Error trying to submit a transaction, please try again later.");
                }
            } else {
                throw new IllegalArgumentException("Error trying to submit a transaction, please try again later");
            }
        } else {
            throw new IllegalArgumentException("Your balance isn't high enough to submit this transaction.");
        }
    }


    private byte[] sign(final RawTransaction etherTransaction) {
        return authenticationService.sign(etherTransaction);
    }


    private RawTransaction generateTransaction(final EthGetTransactionCount transactionCount, final ConfirmEtherTransactionCommand etherTransactionCommand) {
        return RawTransaction.createEtherTransaction(
                transactionCount.getTransactionCount(),
                etherTransactionCommand.getGasPriceInWei(),
                etherTransactionCommand.getGasLimit(),
                etherTransactionCommand.getTo(),
                etherTransactionCommand.getAmountInWei()
        );
    }

    private boolean hasEnoughBalance(final BigInteger balance, final ConfirmEtherTransactionCommand etherTransactionCommand) {
        final BigInteger totalGas = etherTransactionCommand.getGasPriceInWei().multiply(etherTransactionCommand.getGasLimit());
        final BigInteger totalPrice = etherTransactionCommand.getAmountInWei().add(totalGas);
        return (balance.compareTo(totalPrice) >= 0);
    }

    private EthGetTransactionCount calculateNonce(final String address) {
        return web3j.ethGetTransactionCount(
                prettify(address),
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
