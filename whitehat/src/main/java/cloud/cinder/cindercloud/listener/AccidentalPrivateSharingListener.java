package cloud.cinder.cindercloud.listener;

import cloud.cinder.cindercloud.credential.domain.LeakedCredential;
import cloud.cinder.cindercloud.credentials.service.CredentialService;
import cloud.cinder.cindercloud.sweeping.continuous.EthereumSweeperService;
import cloud.cinder.cindercloud.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Numeric;
import rx.Subscription;
import rx.functions.Action1;

import java.util.Date;

@Component
@Slf4j
public class AccidentalPrivateSharingListener {

    @Autowired
    private Web3jGateway web3j;
    @Autowired
    private CredentialService credentialService;

    @Autowired
    private EthereumSweeperService ethereumSweeperConfiguration;

    private Subscription pendingTransactionsSubscription;
    private Subscription liveTransactions;

    @Scheduled(fixedRate = 60000)
    public void pendingTransactions() {
        if (pendingTransactionsSubscription == null) {
            log.trace("[Private Sharing] startup of subscription for accidental private sharing");
            this.pendingTransactionsSubscription = subscribePendingTransactions();
        } else {
            this.pendingTransactionsSubscription.unsubscribe();
            log.trace("[Private Sharing] unsubscribed, resubbing to accidental private sharing");
            this.pendingTransactionsSubscription = subscribePendingTransactions();
        }
    }

    @Scheduled(fixedRate = 60000)
    public void liveTransactions() {
        if (liveTransactions == null) {
            log.trace("[Private Sharing] startup of subscription for accidental private sharing");
            this.liveTransactions = subscribeLiveTransactions();
        } else {
            this.liveTransactions.unsubscribe();
            log.trace("[Private Sharing] unsubscribed, resubbing to accidental private sharing");
            this.liveTransactions = subscribeLiveTransactions();
        }
    }

    private Subscription subscribePendingTransactions() {
        return web3j.web3j().pendingTransactionObservable()
                .subscribe(processTransaction(), error -> {
                    log.debug("[Live Private Sharing]Problem with pending transactions, resubbing: {}", error.getMessage());
                    pendingTransactionsSubscription.unsubscribe();
                    this.pendingTransactionsSubscription = subscribePendingTransactions();
                });
    }

    private Subscription subscribeLiveTransactions() {
        return web3j.web3j().transactionObservable()
                .subscribe(processTransaction(), error -> {
                    log.debug("[Pending Private Sharing]Problem with pending transactions, resubbing: {}", error.getMessage());
                    liveTransactions.unsubscribe();
                    this.liveTransactions = subscribeLiveTransactions();
                });
    }

    private Action1<Transaction> processTransaction() {
        return x -> {

            if (x != null && x.getInput() != null && x.getInput().length() == 66) {
                log.trace("{} might just accidently shared a private", x.getFrom());
                try {
                    final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(x.getInput().replace("\uFEFF", "")));
                    final String address = Keys.getAddress(keypair);
                    credentialService.saveLeakedCredential(
                            LeakedCredential.builder()
                                    .address(prettifyAddress(address))
                                    .privateKey(x.getInput())
                                    .dateAdded(new Date())
                                    .build()
                    );
                } catch (final Exception ex) {
                    log.error("unable to save {} because: {}", x.getInput(), ex.getMessage());
                }
            }
            sweepToIfKnown(x);
        };
    }

    private void sweepToIfKnown(final Transaction x) {
        if (x != null && x.getTo() != null) {
            ethereumSweeperConfiguration.sweepEthereum(x.getTo());
        }
    }


    private String prettifyAddress(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }
}
