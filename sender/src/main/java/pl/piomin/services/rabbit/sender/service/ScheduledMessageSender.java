package pl.piomin.services.rabbit.sender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.piomin.services.rabbit.commons.message.Order;
import pl.piomin.services.rabbit.commons.message.OrderType;

import java.util.Random;

@Service
public class ScheduledMessageSender {

    private static final Logger log = LoggerFactory.getLogger(ScheduledMessageSender.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    private final Random random = new Random();

    // Constructor Injection (as per professor's request)
    public ScheduledMessageSender(RabbitTemplate rabbitTemplate,
                                  @Value("${rabbitmq.queue.name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    /**
     * This method will run automatically every 5 seconds after the application starts.
     */
    @Scheduled(fixedRate = 5000) // sends a message every 5000 milliseconds (5 seconds)
    public void sendOrder() {
        Integer id = random.nextInt(1000);
        OrderType type = (id % 2 == 0) ? OrderType.MODIFY : OrderType.ACTIVATE;
        Order order = new Order(id, "Scheduled order #" + id, type);

        rabbitTemplate.convertAndSend(this.queueName, order);
        log.info("✅ Sent scheduled order to queue: {}", order);
    }
}
