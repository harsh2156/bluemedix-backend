package com.example.bluemedix_backend.service.kafka;


// import com.bluemedix.api.event.OrderEvent;
// import com.bluemedix.api.event.UserRegistrationEvent;
import com.example.bluemedix_backend.event.OrderEvent;
import com.example.bluemedix_backend.event.UserRegistrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "orders", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEvent event) {
        log.info("Received order event: {}", event);
        
        // Process the order event
        // This could involve updating the order status, sending notifications, etc.
        switch (event.getEventType()) {
            case "CREATED":
                log.info("Processing new order: {}", event.getOrderId());
                break;
            case "UPDATED":
                log.info("Processing order update: {}", event.getOrderId());
                break;
            case "CANCELLED":
                log.info("Processing order cancellation: {}", event.getOrderId());
                break;
            default:
                log.warn("Unknown order event type: {}", event.getEventType());
        }
    }

    @KafkaListener(topics = "user-registrations", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserRegistrationEvent(UserRegistrationEvent event) {
        log.info("Received user registration event: {}", event);
        
        // Process the user registration event
        // This could involve sending welcome emails, setting up user profiles, etc.
        switch (event.getEventType()) {
            case "REGISTERED":
                log.info("Processing new user registration: {}", event.getUserId());
                break;
            case "UPDATED":
                log.info("Processing user update: {}", event.getUserId());
                break;
            case "DELETED":
                log.info("Processing user deletion: {}", event.getUserId());
                break;
            default:
                log.warn("Unknown user registration event type: {}", event.getEventType());
        }
    }
}

