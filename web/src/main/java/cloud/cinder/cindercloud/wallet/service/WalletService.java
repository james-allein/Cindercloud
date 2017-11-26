package cloud.cinder.cindercloud.wallet.service;

import cloud.cinder.cindercloud.wallet.domain.GeneratedCredentials;
import cloud.cinder.cindercloud.wallet.domain.PrivateKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.util.Objects;

@Service
public class WalletService {

    private static final ObjectMapper walletmapper = new ObjectMapper();

    static {
        walletmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        walletmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public GeneratedCredentials generateWallet(final String password, final boolean strong) {
        try {
            Objects.requireNonNull(password);
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            final WalletFile walletFile = strong ? Wallet.createStandard(password, ecKeyPair) : Wallet.createLight(password, ecKeyPair);
            return new GeneratedCredentials(walletmapper.writeValueAsString(walletFile), new PrivateKey(ecKeyPair.getPrivateKey()));
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to generate wallet at this point");
        }
    }
}
