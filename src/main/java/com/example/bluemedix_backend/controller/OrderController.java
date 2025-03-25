// package com.example.bluemedix_backend.controller;


// // import com.bluemedix.api.annotation.ApiRateLimited;
// // import com.bluemedix.api.event.OrderEvent;
// // import com.bluemedix.api.message.EmailNotification;
// // import com.bluemedix.api.message.OrderProcessingMessage;
// // import com.bluemedix.api.message.SMSNotification;
// // import com.bluemedix.api.model.Order;
// // import com.bluemedix.api.model.OrderStatus;
// // import com.bluemedix.api.model.User;
// // import com.bluemedix.api.payload.request.OrderRequest;
// // import com.bluemedix.api.payload.response.MessageResponse;
// // import com.bluemedix.api.repository.OrderRepository;
// // import com.bluemedix.api.repository.UserRepository;
// // import com.bluemedix.api.service.kafka.KafkaProducerService;
// // import com.bluemedix.api.service.rabbitmq.RabbitMQProducerService;
// import com.example.bluemedix_backend.annotation.ApiRateLimited;
// import com.example.bluemedix_backend.event.OrderEvent;
// import com.example.bluemedix_backend.message.EmailNotification;
// import com.example.bluemedix_backend.message.OrderProcessingMessage;
// import com.example.bluemedix_backend.message.SMSNotification;
// import com.example.bluemedix_backend.model.ERole;
// import com.example.bluemedix_backend.model.Order;
// import com.example.bluemedix_backend.model.OrderStatus;
// import com.example.bluemedix_backend.model.User;
// import com.example.bluemedix_backend.payload.request.OrderRequest;
// import com.example.bluemedix_backend.repository.OrderRepository;
// import com.example.bluemedix_backend.repository.UserRepository;
// import com.example.bluemedix_backend.payload.response.MessageResponse;
// import com.example.bluemedix_backend.service.kafka.KafkaProducerService;
// import com.example.bluemedix_backend.service.rabbitmq.RabbitMQProducerService;


// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
// import java.util.List;

// @CrossOrigin(origins = "*", maxAge = 3600)
// @RestController
// @RequestMapping("/orders")
// @RequiredArgsConstructor
// public class OrderController {
//     private final OrderRepository orderRepository;
//     private final UserRepository userRepository;
//     private final KafkaProducerService kafkaProducerService;
//     private final RabbitMQProducerService rabbitMQProducerService;

//     @GetMapping
//     @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//     @ApiRateLimited
//     public ResponseEntity<List<Order>> getAllOrders() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         String username = authentication.getName();
//         User user = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new RuntimeException("Error: User not found."));

//         List<Order> orders;
//         if (user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
//             orders = orderRepository.findAll();
//         } else {
//             orders = orderRepository.findByUserId(user.getId());
//         }

//         return ResponseEntity.ok(orders);
//     }

//     @GetMapping("/{id}")
//     @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//     @ApiRateLimited
//     public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//         Order order = orderRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Error: Order not found."));

//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         String username = authentication.getName();
//         User user = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new RuntimeException("Error: User not found."));

//         if (!user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN) &&
//                 !order.getUser().getId().equals(user.getId())) {
//             return ResponseEntity.status(403).build();
//         }

//         return ResponseEntity.ok(order);
//     }

//     @PostMapping
//     @PreAuthorize("hasRole('USER')")
//     @ApiRateLimited
//     public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         String username = authentication.getName();
//         User user = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new RuntimeException("Error: User not found."));

//         Order order = new Order();
//         order.setUser(user);
//         order.setStatus(OrderStatus.PENDING);
//         order.setOrderDate(LocalDateTime.now());
//         // Set other order properties from orderRequest

//         Order savedOrder = orderRepository.save(order);

//         // Send Kafka event
//         OrderEvent event = OrderEvent.fromOrder(savedOrder, "CREATED");
//         kafkaProducerService.sendOrderEvent(event);

//         // Send order processing message via RabbitMQ
//         OrderProcessingMessage processingMessage = new OrderProcessingMessage(
//                 savedOrder.getId(),
//                 user.getId(),
//                 savedOrder.getOrderDate(),
//                 savedOrder.getStatus().toString()
//         );
//         rabbitMQProducerService.sendOrderProcessingMessage(processingMessage);

//         // Send order confirmation email
//         EmailNotification emailNotification = new EmailNotification(
//                 user.getEmail(),
//                 "Order Confirmation - BlueMedix",
//                 "Your order has been placed successfully!",
//                 "order-confirmation-template",
//                 savedOrder
//         );
//         rabbitMQProducerService.sendEmailNotification(emailNotification);

//         // Send order confirmation SMS if phone number is available
//         if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
//             SMSNotification smsNotification = new SMSNotification(
//                     user.getPhoneNumber(),
//                     "Your BlueMedix order #" + savedOrder.getId() + " has been placed successfully!"
//             );
//             rabbitMQProducerService.sendSMSNotification(smsNotification);
//         }

//         return ResponseEntity.ok(new MessageResponse("Order created successfully!"));
//     }

//     @PutMapping("/{id}/status")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
//     @ApiRateLimited
//     public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
//         Order order = orderRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Error: Order not found."));

//         order.setStatus(status);
//         Order updatedOrder = orderRepository.save(order);

//         // Send Kafka event
//         OrderEvent event = OrderEvent.fromOrder(updatedOrder, "UPDATED");
//         kafkaProducerService.sendOrderEvent(event);

//         // Send status update email
//         EmailNotification emailNotification = new EmailNotification(
//                 updatedOrder.getUser().getEmail(),
//                 "Order Status Update - BlueMedix",
//                 "Your order status has been updated to " + status,
//                 "order-status-template",
//                 updatedOrder
//         );
//         rabbitMQProducerService.sendEmailNotification(emailNotification);

//         return ResponseEntity.ok(new MessageResponse("Order status updated successfully!"));
//     }
// }


package com.example.bluemedix_backend.controller;

import com.example.bluemedix_backend.annotation.ApiRateLimited;
import com.example.bluemedix_backend.event.OrderEvent;
import com.example.bluemedix_backend.message.EmailNotification;
import com.example.bluemedix_backend.message.OrderProcessingMessage;
import com.example.bluemedix_backend.message.SMSNotification;
import com.example.bluemedix_backend.model.ERole;
import com.example.bluemedix_backend.model.Order;
import com.example.bluemedix_backend.model.OrderStatus;
import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.payload.request.OrderRequest;
import com.example.bluemedix_backend.payload.response.MessageResponse;
import com.example.bluemedix_backend.repository.OrderRepository;
import com.example.bluemedix_backend.repository.UserRepository;
import com.example.bluemedix_backend.service.kafka.KafkaProducerService;
import com.example.bluemedix_backend.service.rabbitmq.RabbitMQProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private final RabbitMQProducerService rabbitMQProducerService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ApiRateLimited
    public ResponseEntity<List<Order>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming authentication name is the email address
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        List<Order> orders;
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN))) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByUserId(user.getId());
        }

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ApiRateLimited
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Order not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (!user.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN)) &&
                !order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(order);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiRateLimited
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        // No need to call setOrderDate() because 'createdAt' is automatically populated


        Order savedOrder = orderRepository.save(order);

        // Send Kafka event
        OrderEvent event = OrderEvent.fromOrder(savedOrder, "CREATED");
        kafkaProducerService.sendOrderEvent(event);

        // Send order processing message via RabbitMQ using createdAt as the order date
        OrderProcessingMessage processingMessage = new OrderProcessingMessage(
                savedOrder.getId(),
                user.getId(),
                savedOrder.getCreatedAt(),
                savedOrder.getStatus().toString()
        );
        rabbitMQProducerService.sendOrderProcessingMessage(processingMessage);

        // Send order confirmation email
        EmailNotification emailNotification = new EmailNotification(
                user.getEmail(),
                "Order Confirmation - BlueMedix",
                "Your order has been placed successfully!",
                "order-confirmation-template",
                savedOrder
        );
        rabbitMQProducerService.sendEmailNotification(emailNotification);

        // Send order confirmation SMS if phone is available (using getPhone() instead of getPhoneNumber())
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            SMSNotification smsNotification = new SMSNotification(
                    user.getPhone(),
                    "Your BlueMedix order #" + savedOrder.getId() + " has been placed successfully!"
            );
            rabbitMQProducerService.sendSMSNotification(smsNotification);
        }

        return ResponseEntity.ok(new MessageResponse("Order created successfully!"));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    @ApiRateLimited
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Order not found."));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // Send Kafka event
        OrderEvent event = OrderEvent.fromOrder(updatedOrder, "UPDATED");
        kafkaProducerService.sendOrderEvent(event);

        // Send status update email
        EmailNotification emailNotification = new EmailNotification(
                updatedOrder.getUser().getEmail(),
                "Order Status Update - BlueMedix",
                "Your order status has been updated to " + status,
                "order-status-template",
                updatedOrder
        );
        rabbitMQProducerService.sendEmailNotification(emailNotification);

        return ResponseEntity.ok(new MessageResponse("Order status updated successfully!"));
    }
}
