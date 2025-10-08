package pl.piomin.services.rabbit.listener.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerConfig {

    // You can get the queue name directly from application.properties
    @Bean
    public Queue ordersQueue(@Value("${rabbitmq.queue.name}") String queueName) {
        return new Queue(queueName, true);
    }
}