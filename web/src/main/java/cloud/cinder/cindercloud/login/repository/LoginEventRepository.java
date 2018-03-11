package cloud.cinder.cindercloud.login.repository;

import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import cloud.cinder.cindercloud.login.domain.LoginEvent;

public interface LoginEventRepository extends JpaRepository<LoginEvent, Long> {
}
