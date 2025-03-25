package com.example.bluemedix_backend.service.rabbitmq;


// import com.bluemedix.api.config.RabbitMQConfig;
// import com.bluemedix.api.message.EmailNotification;
// import com.bluemedix.api.message.OrderProcessingMessage;
// import com.bluemedix.api.message.SMSNotification;
import com.example.bluemedix_backend.config.RabbitMQConfig;
import com.example.bluemedix_backend.message.EmailNotification;
import com.example.bluemedix_backend.message.OrderProcessingMessage;
import com.example.bluemedix_backend.message.SMSNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmailNotification(EmailNotification notification) {
        log.info("Sending email notification to RabbitMQ: {}", notification);
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                notification
        );
    }

    public void sendSMSNotification(SMSNotification notification) {
        log.info("Sending SMS notification to RabbitMQ: {}", notification);
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.SMS_ROUTING_KEY,
                notification
        );
    }

    public void sendOrderProcessingMessage(OrderProcessingMessage message) {
        log.info("Sending order processing message to RabbitMQ: {}", message);
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_PROCESSING_ROUTING_KEY,
                message
        );
    }
}

