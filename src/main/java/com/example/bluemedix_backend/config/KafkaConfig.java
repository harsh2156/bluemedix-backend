package com.example.bluemedix_backend.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name("orders")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic medicineRequestsTopic() {
        return TopicBuilder.name("medicine-requests")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userRegistrationsTopic() {
        return TopicBuilder.name("user-registrations")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic franchiseRegistrationsTopic() {
        return TopicBuilder.name("franchise-registrations")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic prescriptionsTopic() {
        return TopicBuilder.name("prescriptions")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic healthCheckTopic() {
        return TopicBuilder.name("health-check")
                .partitions(1)
                .replicas(1)
                .build();
    }
}

