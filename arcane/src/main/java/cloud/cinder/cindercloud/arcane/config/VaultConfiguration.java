package cloud.cinder.cindercloud.arcane.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.support.VaultToken;

@Configuration
public class VaultConfiguration extends AbstractVaultConfiguration {

    @Value("${cloud.cinder.vault.host}")
    private String vaultHost;

    @Value("${cloud.cinder.vault.port:8200}")
    private int vaultPort;

    @Value("${cloud.cinder.vault.root-token}")
    private String vaultToken;

    @Override
    public VaultEndpoint vaultEndpoint() {
        final VaultEndpoint vaultEndpoint = VaultEndpoint.create(vaultHost, vaultPort);
        vaultEndpoint.setScheme("http");
        return vaultEndpoint;
    }

    @Override
    public ClientAuthentication clientAuthentication() {
        return new TokenAuthentication(VaultToken.of(vaultToken));
    }
}
