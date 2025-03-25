package com.example.bluemedix_backend.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProcessingMessage implements Serializable {
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
}

