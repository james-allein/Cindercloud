package cloud.cinder.cindercloud.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(final String queue, final String message) {
        log.trace("sending message on queue");
        this.rabbitTemplate.convertAndSend(queue, message);
    }
}
