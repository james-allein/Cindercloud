package cloud.cinder.cindercloud.login.event;

import cloud.cinder.cindercloud.login.domain.LoginEvent;
import cloud.cinder.cindercloud.login.repository.LoginEventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoginEventHandler {

    private final LoginEventRepository loginEventRepository;

    public LoginEventHandler(final LoginEventRepository loginEventRepository) {
        this.loginEventRepository = loginEventRepository;
    }

    @EventListener
    @Transactional
    public void loginOccurred(final LoginEvent loginEvent) {
        loginEventRepository.save(loginEvent);
    }
}