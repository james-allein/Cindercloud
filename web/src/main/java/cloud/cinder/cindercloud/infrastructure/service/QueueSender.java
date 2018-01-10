package cloud.cinder.cindercloud.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueSender {

    @Value("${cloud.cinder.sqs.name}")
    private String blockQueue;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message, String eventType) {
        log.trace("sending message on queue");
        this.rabbitTemplate.convertAndSend(blockQueue, message);
    }
}
