package cloud.cinder.cindercloud.wallet.service;

import cloud.cinder.cindercloud.security.domain.AuthenticationType;
import cloud.cinder.cindercloud.security.domain.PrivateKeyAuthentication;
import cloud.cinder.cindercloud.security.domain.Web3Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    public void requireAuthenticated() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            log.trace("Logged in using private key");
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof Web3Authentication)) {
            log.trace("Logged in using web3 authentication");
        } else {
            throw new IllegalArgumentException("Not authenticated");
        }
    }

    public AuthenticationType getType() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            log.trace("Logged in using private key");
            return AuthenticationType.CINDERCLOUD;
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof Web3Authentication)) {
            log.trace("Logged in using web3 authentication");
            return AuthenticationType.WEB3;
        } else {
            throw new IllegalArgumentException("Not authenticated");
        }
    }
}
