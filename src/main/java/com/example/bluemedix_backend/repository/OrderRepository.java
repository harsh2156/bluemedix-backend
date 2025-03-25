package com.example.bluemedix_backend.repository;

// package com.bluemedix.api.repository;

// import com.bluemedix.api.model.Order;
// import com.bluemedix.api.model.OrderStatus;
// import com.bluemedix.api.model.User;
// import com.bluemedix.api.model.Warehouse;
import com.example.bluemedix_backend.model.Order;
import com.example.bluemedix_backend.model.OrderStatus;
import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByAssignedWarehouse(Warehouse warehouse);
    List<Order> findByAssignedWarehouseAndStatus(Warehouse warehouse, OrderStatus status);

    // Added to support finding orders by user id
    List<Order> findByUserId(Long userId);
}

