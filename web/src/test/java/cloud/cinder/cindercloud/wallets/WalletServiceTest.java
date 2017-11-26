package cloud.cinder.cindercloud.wallets;

import cloud.cinder.cindercloud.wallet.domain.GeneratedCredentials;
import cloud.cinder.cindercloud.wallet.service.WalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Test
    public void createWallet() throws Exception {
        final GeneratedCredentials result = walletService.generateWallet("", true);
        assertThat(result).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWalletWithoutPassword() throws Exception {
        walletService.generateWallet(null, true);
    }
}