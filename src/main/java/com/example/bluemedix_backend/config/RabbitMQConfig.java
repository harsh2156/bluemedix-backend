package com.example.bluemedix_backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String EMAIL_QUEUE = "email-notification-queue";
    public static final String SMS_QUEUE = "sms-notification-queue";
    public static final String PUSH_NOTIFICATION_QUEUE = "push-notification-queue";
    public static final String ORDER_PROCESSING_QUEUE = "order-processing-queue";
    public static final String ORDER_FULFILLMENT_QUEUE = "order-fulfillment-queue";
    
    // Exchange names
    public static final String NOTIFICATION_EXCHANGE = "notification-exchange";
    public static final String ORDER_EXCHANGE = "order-exchange";
    
    // Routing keys
    public static final String EMAIL_ROUTING_KEY = "notification.email";
    public static final String SMS_ROUTING_KEY = "notification.sms";
    public static final String PUSH_NOTIFICATION_ROUTING_KEY = "notification.push";
    public static final String ORDER_PROCESSING_ROUTING_KEY = "order.processing";
    public static final String ORDER_FULFILLMENT_ROUTING_KEY = "order.fulfillment";

    // Queues
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx")
                .withArgument("x-dead-letter-routing-key", "dlq.email")
                .build();
    }

    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(SMS_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx")
                .withArgument("x-dead-letter-routing-key", "dlq.sms")
                .build();
    }

    @Bean
    public Queue pushNotificationQueue() {
        return QueueBuilder.durable(PUSH_NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx")
                .withArgument("x-dead-letter-routing-key", "dlq.push")
                .build();
    }

    @Bean
    public Queue orderProcessingQueue() {
        return QueueBuilder.durable(ORDER_PROCESSING_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx")
                .withArgument("x-dead-letter-routing-key", "dlq.order.processing")
                .build();
    }

    @Bean
    public Queue orderFulfillmentQueue() {
        return QueueBuilder.durable(ORDER_FULFILLMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx")
                .withArgument("x-dead-letter-routing-key", "dlq.order.fulfillment")
                .build();
    }

    // Dead Letter Queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dead-letter-queue").build();
    }

    // Exchanges
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx");
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // Bindings
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(notificationExchange()).with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue()).to(notificationExchange()).with(SMS_ROUTING_KEY);
    }

    @Bean
    public Binding pushNotificationBinding() {
        return BindingBuilder.bind(pushNotificationQueue()).to(notificationExchange()).with(PUSH_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding orderProcessingBinding() {
        return BindingBuilder.bind(orderProcessingQueue()).to(orderExchange()).with(ORDER_PROCESSING_ROUTING_KEY);
    }

    @Bean
    public Binding orderFulfillmentBinding() {
        return BindingBuilder.bind(orderFulfillmentQueue()).to(orderExchange()).with(ORDER_FULFILLMENT_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dlq.*");
    }

    // Message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}


