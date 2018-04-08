package cloud.cinder.cindercloud.wallet.controller.command.login;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;

@Data
@NoArgsConstructor
public class TrezorLoginCommand {

    private String hiddenChallenge;
    private String visualChallenge;
    private String public_key;
    private String signature;

    public ECKey getPublicKey() {
        return ECKey.fromPublicOnly(Hex.decode(public_key));
    }

    public byte[] getMessage() {
        try {
            return Arrays.concatenate(Sha256Hash.hash(Hex.decode(hiddenChallenge)), Sha256Hash.hash(visualChallenge.getBytes("ASCII")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to decode visual challenge");
        }
    }
}
