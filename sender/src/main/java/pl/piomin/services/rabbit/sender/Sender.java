package pl.piomin.services.rabbit.sender;

import java.util.Random;
import java.util.logging.Logger;

// These are now required for the scheduled task functionality
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

// Removed javax.annotation.PostConstruct as it's no longer used
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.piomin.services.rabbit.commons.message.Order;
import pl.piomin.services.rabbit.commons.message.OrderType;

@SpringBootApplication
@EnableScheduling // This annotation enables Spring's task scheduling capabilities
public class Sender {

    private static Logger logger = Logger.getLogger("Sender");

    // Spring Boot will automatically inject the auto-configured RabbitTemplate here
    @Autowired
    private RabbitTemplate template;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    public static void main(String[] args) {
        SpringApplication.run(Sender.class, args);
    }

    // This method will now execute at a fixed rate, instead of just once on startup.
    // The fixedRate = 100 sets the execution to every 100 milliseconds,
    // which corresponds to a message rate of 10 messages per second.
    // Removed @PostConstruct annotation.
    @Scheduled(fixedRate = 100)
    public void send() {
        String exchangeName = "";
        String routingKey = queueName;

        int id = new Random().nextInt(100000);
        template.convertAndSend(exchangeName, routingKey, new Order(id, "TEST"+id, OrderType.values()[(id%2)]));

        logger.info("Sent message: TEST" + id);
    }
}
