package pl.piomin.services.rabbit.listener;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * This bean defines the queue that the listener will connect to.
     */
    @Bean
    public Queue queue(@Value("${rabbitmq.queue.name}") String queueName) {
        return new Queue(queueName);
    }

    /**
     * THIS IS THE FINAL FIX.
     * This bean tells Spring AMQP which classes are safe to deserialize.
     */
    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        // We are now whitelisting both our custom message package AND java.lang.Enum
        converter.addAllowedListPatterns(
                "pl.piomin.services.rabbit.commons.message.*",
                "java.lang.Enum"
        );
        return converter;
    }
}