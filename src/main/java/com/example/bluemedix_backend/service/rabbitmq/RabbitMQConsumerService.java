package com.example.bluemedix_backend.service.rabbitmq;


// import com.bluemedix.api.config.RabbitMQConfig;
import com.example.bluemedix_backend.config.RabbitMQConfig;
// import com.bluemedix.api.message.EmailNotification;
import com.example.bluemedix_backend.message.EmailNotification;
// import com.bluemedix.api.message.OrderProcessingMessage;
import com.example.bluemedix_backend.message.OrderProcessingMessage;
// import com.bluemedix.api.message.SMSNotification;
import com.example.bluemedix_backend.message.SMSNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQConsumerService {

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEmailNotification(EmailNotification notification) {
        log.info("Received email notification: {}", notification);
        
        // Process the email notification
        // This would typically involve sending an actual email
        log.info("Sending email to: {}, subject: {}", notification.getTo(), notification.getSubject());
        
        // Simulate email sending
        try {
            Thread.sleep(100); // Simulate processing time
            log.info("Email sent successfully to: {}", notification.getTo());
        } catch (InterruptedException e) {
            log.error("Error while processing email notification: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.SMS_QUEUE)
    public void consumeSMSNotification(SMSNotification notification) {
        log.info("Received SMS notification: {}", notification);
        
        // Process the SMS notification
        // This would typically involve sending an actual SMS
        log.info("Sending SMS to: {}, message: {}", notification.getPhoneNumber(), notification.getMessage());
        
        // Simulate SMS sending
        try {
            Thread.sleep(100); // Simulate processing time
            log.info("SMS sent successfully to: {}", notification.getPhoneNumber());
        } catch (InterruptedException e) {
            log.error("Error while processing SMS notification: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_PROCESSING_QUEUE)
    public void consumeOrderProcessingMessage(OrderProcessingMessage message) {
        log.info("Received order processing message: {}", message);
        
        // Process the order
        // This could involve inventory checks, payment processing, etc.
        log.info("Processing order: {}", message.getOrderId());
        
        // Simulate order processing
        try {
            Thread.sleep(200); // Simulate processing time
            log.info("Order processed successfully: {}", message.getOrderId());
        } catch (InterruptedException e) {
            log.error("Error while processing order: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}


