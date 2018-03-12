package cloud.cinder.cindercloud.event.repository;

import cloud.cinder.cindercloud.event.domain.Event;
import cloud.cinder.cindercloud.event.domain.EventType;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    long countByType(final @Param("type") EventType type);
}
