package com.example.bluemedix_backend.service.kafka;


// import com.bluemedix.api.event.OrderEvent;
// import com.bluemedix.api.event.UserRegistrationEvent;
import com.example.bluemedix_backend.event.OrderEvent;
import com.example.bluemedix_backend.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderEvent(OrderEvent event) {
        String topic = "orders";
        String key = event.getOrderId().toString();
        
        log.info("Sending order event to Kafka: {}", event);
        
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Order event sent successfully: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send order event: {}", ex.getMessage(), ex);
            }
        });
    }

    public void sendUserRegistrationEvent(UserRegistrationEvent event) {
        String topic = "user-registrations";
        String key = event.getUserId().toString();
        
        log.info("Sending user registration event to Kafka: {}", event);
        
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("User registration event sent successfully: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send user registration event: {}", ex.getMessage(), ex);
            }
        });
    }
}

