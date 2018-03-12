package cloud.cinder.cindercloud.login.handler;

import cloud.cinder.cindercloud.login.domain.LoginEvent;
import cloud.cinder.cindercloud.security.domain.ClientSideAuthentication;
import cloud.cinder.cindercloud.security.domain.PrivateKeyAuthentication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

import java.util.Date;

import static cloud.cinder.cindercloud.utils.AddressUtils.prettifyAddress;

@Component
public class LoginHandler {

    private ApplicationEventPublisher $;

    public LoginHandler(final ApplicationEventPublisher applicationEventPublisher) {
        this.$ = applicationEventPublisher;
    }

    public void login(final Credentials credentials) {
        SecurityContextHolder.getContext().setAuthentication(new PrivateKeyAuthentication(credentials.getEcKeyPair(), prettifyAddress(credentials.getAddress())));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(credentials.getAddress(), "CINDERCLOUD");
    }

    public void clientsideLogin(final String address) {
        SecurityContextHolder.getContext().setAuthentication(new ClientSideAuthentication(prettifyAddress(address)));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(address, "WEB3");
    }


    private void throwEvent(final String address, final String walletType) {
        $.publishEvent(
                LoginEvent
                        .builder()
                        .wallet(address)
                        .eventTime(new Date())
                        .walletType(walletType)
                        .build()
        );
    }
}
