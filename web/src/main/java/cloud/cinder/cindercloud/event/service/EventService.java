package cloud.cinder.cindercloud.event.service;

import cloud.cinder.cindercloud.event.domain.Event;
import cloud.cinder.cindercloud.event.domain.EventType;
import cloud.cinder.cindercloud.event.repository.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private EventRepository eventRepository;

    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    @Transactional
    public void saveEvent(final Event event) {
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Long countByType(final EventType eventType) {
        return eventRepository.countByType(eventType);
    }
}
