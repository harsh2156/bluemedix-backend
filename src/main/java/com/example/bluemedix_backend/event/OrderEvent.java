package com.example.bluemedix_backend.event;


// import com.bluemedix.api.model.Order;
import com.example.bluemedix_backend.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private String status;
    private LocalDateTime timestamp;
    private String eventType;
    
    public static OrderEvent fromOrder(Order order, String eventType) {
        return new OrderEvent(
                order.getId(),
                order.getUser().getId(),
                order.getStatus().toString(),
                LocalDateTime.now(),
                eventType
        );
    }
}

