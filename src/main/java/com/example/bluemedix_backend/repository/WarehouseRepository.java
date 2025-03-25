package com.example.bluemedix_backend.repository;

import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByManager(User manager);
    
    @Query(value = "SELECT * FROM warehouse w ORDER BY POWER(CAST(w.pincode AS UNSIGNED) - :pincode, 2) ASC", nativeQuery = true)
    List<Warehouse> findNearestByPincode(Integer pincode);
}
