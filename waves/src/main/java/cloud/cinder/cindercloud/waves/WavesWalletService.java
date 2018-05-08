package cloud.cinder.cindercloud.waves;

import com.wavesplatform.wavesj.Account;
import com.wavesplatform.wavesj.PrivateKeyAccount;
import org.springframework.stereotype.Component;

@Component
public class WavesWalletService {

    public String generateSeed() {
        return PrivateKeyAccount.generateSeed();
    }

    public PrivateKeyAccount generateAccount(final String seed) {
        return PrivateKeyAccount.fromSeed(seed, 0, Account.MAINNET);
    }
}
