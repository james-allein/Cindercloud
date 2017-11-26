package cloud.cinder.cindercloud.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.web3j.crypto.ECKeyPair;

import static java.util.Collections.emptyList;

public class PrivateKeyAuthentication extends AbstractAuthenticationToken {

    final ECKeyPair keyPair;
    final String address;

    public PrivateKeyAuthentication(final ECKeyPair keyPair, final String address) {
        super(emptyList());
        this.keyPair = keyPair;
        this.address = address;
    }

    @Override
    public Object getCredentials() {
        return keyPair;
    }

    @Override
    public Object getPrincipal() {
        return address;
    }
}
