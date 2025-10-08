package pl.piomin.services.rabbit.sender.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {

    @Bean
    public Queue ordersQueue(@Value("${rabbitmq.queue.name}") String queueName) {
        return new Queue(queueName, true);
    }
}