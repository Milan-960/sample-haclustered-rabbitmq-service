package pl.piomin.services.rabbit.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableRabbit
public class Listener {
    public static void main(String[] args) {
        SpringApplication.run(Listener.class, args);
    }
}