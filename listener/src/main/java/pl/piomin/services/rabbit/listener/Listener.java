package pl.piomin.services.rabbit.listener;

import java.util.logging.Logger;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
// Import the necessary classes for MessageConverter
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter; // For Java Serialization

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.piomin.services.rabbit.commons.message.Order;

@SpringBootApplication
@EnableRabbit
public class Listener {

    private static Logger logger = Logger.getLogger("Listener");

    private Long timestamp;

    public static void main(String[] args) {
        SpringApplication.run(Listener.class, args);
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void onMessage(Order order) {
        if (timestamp == null)
            timestamp = System.currentTimeMillis();
        logger.info((System.currentTimeMillis() - timestamp) + " : " + order.toString());
    }

    @Bean
    public Queue queue() {
        return new Queue("${rabbitmq.queue.name}");
    }

    // This bean explicitly tells Spring AMQP to allow deserialization of classes
    // within the 'pl.piomin.services.rabbit.commons.message' package.
    // This resolves the 'SecurityException: Attempt to deserialize unauthorized class' error.
    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        // Whitelist your custom message class's package
        converter.addAllowedListPatterns("pl.piomin.services.rabbit.commons.message.*");
        // Whitelist java.lang.Enum, as it's part of deserializing your OrderType enum
        converter.addAllowedListPatterns("java.lang.Enum");
        return converter;
    }
}