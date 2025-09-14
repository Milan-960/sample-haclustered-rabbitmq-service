package pl.piomin.services.rabbit.sender;

import java.util.Random;
import java.util.logging.Logger;

// Changed from javax.annotation to jakarta.annotation for Spring Boot 3.x compatibility
import jakarta.annotation.PostConstruct;

// Removed unnecessary imports for manually defined beans
// import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean; // Removed as no @Bean methods remain in this class

import pl.piomin.services.rabbit.commons.message.Order;
import pl.piomin.services.rabbit.commons.message.OrderType;

@SpringBootApplication
public class Sender {

    private static Logger logger = Logger.getLogger("Sender");

    // Spring Boot will automatically inject the auto-configured RabbitTemplate here
    @Autowired
    private RabbitTemplate template; // Use 'private' for good practice

    public static void main(String[] args) {
        SpringApplication.run(Sender.class, args);
    }

    @PostConstruct
    public void send() {
        // Assuming 'q.example' is bound to the default exchange with routing key 'q.example'
        // This is the standard way to send messages to a queue when not using a custom exchange explicitly
        String exchangeName = ""; // Default exchange
        String routingKey = "q.example"; // Routing key is the queue name for direct exchange binding

        for (int i = 0; i < 100000; i++) {
            int id = new Random().nextInt(100000);
            template.convertAndSend(exchangeName, routingKey, new Order(id, "TEST"+id, OrderType.values()[(id%2)]));
        }
        logger.info("Sending completed.");
    }

    // REMOVED: ConnectionFactory and RabbitTemplate @Bean methods.
    // Spring Boot will now auto-configure these based on application.properties.
    /*
    @Bean
    public ConnectionFactory connectionFactory() {
        // ... (removed content)
    }

    @Bean
    public RabbitTemplate template() {
        // ... (removed content)
    }
    */
}