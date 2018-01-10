package cloud.cinder.cindercloud.config;

import cloud.cinder.cindercloud.block.continuous.BlockAddedListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    @Bean
    @ConditionalOnProperty(name = "cloud.cinder.ethereum.sqs-transaction-import", havingValue = "true")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter,
                                             @Value("${cloud.cinder.sqs.name}") final String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(5);
        return container;
    }

    @Bean
    @ConditionalOnProperty(name = "cloud.cinder.ethereum.sqs-transaction-import", havingValue = "true")
    MessageListenerAdapter listenerAdapter(BlockAddedListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
