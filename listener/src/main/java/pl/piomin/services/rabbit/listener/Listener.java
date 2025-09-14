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

    @RabbitListener(queues = "q.example")
    public void onMessage(Order order) {
        if (timestamp == null)
            timestamp = System.currentTimeMillis();
        logger.info((System.currentTimeMillis() - timestamp) + " : " + order.toString());
    }

    // REMOVE OR COMMENT OUT THESE BEANS. Spring Boot will configure them
    // based on your application.properties/application.yml
    /*
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // These addresses are likely from a different environment or legacy setup
        // Rely on spring.rabbitmq.addresses in application.properties/yml instead
        connectionFactory.setAddresses("192.168.99.100:30000,192.168.99.100:30002,192.168.99.100:30004");
        connectionFactory.setChannelCacheSize(10);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory()); // This would use the hardcoded one
        factory.setConcurrentConsumers(10);
        factory.setMaxConcurrentConsumers(20);
        return factory;
    }
    */

    @Bean
    public Queue queue() {
        return new Queue("q.example");
    }

    // --- ADD THIS NEW BEAN METHOD ---
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