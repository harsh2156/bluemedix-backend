package com.example.bluemedix_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    private final DataSource dataSource;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final HealthIndicator rabbitHealthIndicator;

    public HealthController(DataSource dataSource,
                            KafkaTemplate<String, String> kafkaTemplate,
                            @Qualifier("pingHealthContributor") HealthIndicator rabbitHealthIndicator) {
        this.dataSource = dataSource;
        this.kafkaTemplate = kafkaTemplate;
        this.rabbitHealthIndicator = rabbitHealthIndicator;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> healthStatus = new HashMap<>();
        
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        components.put("database", checkDatabase());
        components.put("kafka", checkKafka());
        components.put("rabbitmq", checkRabbitMQ());
        
        healthStatus.put("components", components);
        
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> checkDatabase() {
        Map<String, Object> status = new HashMap<>();
        status.put("component", "database");
        
        try (Connection connection = dataSource.getConnection()) {
            status.put("status", "UP");
            status.put("details", "Database connection successful");
        } catch (Exception e) {
            log.error("Database health check failed: {}", e.getMessage(), e);
            status.put("status", "DOWN");
            status.put("details", "Database connection failed: " + e.getMessage());
        }
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/kafka")
    public ResponseEntity<Map<String, Object>> checkKafka() {
        Map<String, Object> status = new HashMap<>();
        status.put("component", "kafka");
        
        try {
            kafkaTemplate.send("health-check", "health-check-" + System.currentTimeMillis());
            status.put("status", "UP");
            status.put("details", "Kafka connection successful");
        } catch (Exception e) {
            log.error("Kafka health check failed: {}", e.getMessage(), e);
            status.put("status", "DOWN");
            status.put("details", "Kafka connection failed: " + e.getMessage());
        }
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/rabbitmq")
    public ResponseEntity<Map<String, Object>> checkRabbitMQ() {
        Map<String, Object> status = new HashMap<>();
        status.put("component", "rabbitmq");
        
        try {
            Health health = rabbitHealthIndicator.health();
            status.put("status", health.getStatus().getCode());
            status.put("details", health.getDetails());
        } catch (Exception e) {
            log.error("RabbitMQ health check failed: {}", e.getMessage(), e);
            status.put("status", "DOWN");
            status.put("details", "RabbitMQ connection failed: " + e.getMessage());
        }
        
        return ResponseEntity.ok(status);
    }
}
