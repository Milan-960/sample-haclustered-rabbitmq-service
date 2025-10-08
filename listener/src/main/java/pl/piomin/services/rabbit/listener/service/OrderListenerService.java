package pl.piomin.services.rabbit.listener.service;

// Import the SLF4J classes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.piomin.services.rabbit.commons.message.Order;

@Service
public class OrderListenerService {

    // Manually define the logger instead of using @Slf4j
    private static final Logger log = LoggerFactory.getLogger(OrderListenerService.class);

    private Long timestamp;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receive(Order order) {
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }
        long processingTime = System.currentTimeMillis() - timestamp;
        log.info("✅ Received order after {} ms: {}", processingTime, order);
        timestamp = System.currentTimeMillis();
    }
}