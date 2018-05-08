package cloud.cinder.cindercloud.waves;

import com.wavesplatform.wavesj.PrivateKeyAccount;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WavesAccountServiceTest {

    private WavesWalletService walletService;

    @Before
    public void setUp() throws Exception {
        walletService = new WavesWalletService();
    }

    @Test
    public void scenario1() {
        final PrivateKeyAccount privateKeyAccount = walletService.generateAccount("kidney better lounge wool trophy that mountain concert random warm agent finger point snow pact");
        assertThat(privateKeyAccount.getAddress()).isEqualTo("3PEZJPFBXThZF9AmCvZ5cWHoHAwPxjr5MPX");
    }
}