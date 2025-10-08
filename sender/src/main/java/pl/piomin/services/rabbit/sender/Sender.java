package pl.piomin.services.rabbit.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // This annotation enables the @Scheduled task in our new service
public class Sender {

    public static void main(String[] args) {
        SpringApplication.run(Sender.class, args);
    }

}