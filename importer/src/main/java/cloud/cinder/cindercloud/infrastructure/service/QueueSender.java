package cloud.cinder.cindercloud.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cloud.cinder.queue.block-added}")
    private String blockQueue;

    public void send(String message, String eventType) {
        log.trace("sending message on queue");
        this.rabbitTemplate.convertAndSend(blockQueue, message);
    }
}