package cloud.cinder.cindercloud.infrastructure.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SqsQueueSender {

    @Value("${cloud.cinder.sqs.name}")
    private String blockQueue;

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public SqsQueueSender(AmazonSQSAsync amazonSqs) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
    }

    public void send(String message, String eventType) {
        this.queueMessagingTemplate.send(blockQueue, MessageBuilder.withPayload(message).setHeader("event_type", eventType).build());
    }
}
