package cloud.cinder.cindercloud.bip39;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.IntStream;

import static cloud.cinder.cindercloud.bip39.MnemonicUtils.sha256;

public class MnemonicUtilsTest {

    final SecureRandom secureRandom = new SecureRandom();

    @Test
    public void name() throws Exception {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        System.out.println(mnemonic);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        for(int i = 0; i < 100_000; i ++) {
            seed = sha256(seed);
        }
        ECKeyPair keypair = ECKeyPair.create(sha256(seed));
        System.out.println(Keys.getAddress(keypair.getPublicKey()));

        Wallet wallet = Wallet.fromSeed(NetworkParameters.fromID("m/44'/60'/0'/0"), new DeterministicSeed(seed, "", 1));
        List<ECKey> $keys = wallet.getImportedKeys();
    }
}