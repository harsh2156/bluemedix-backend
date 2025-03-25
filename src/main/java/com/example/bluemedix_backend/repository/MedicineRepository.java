package com.example.bluemedix_backend.repository;


// import com.bluemedix.api.model.Category;
// import com.bluemedix.api.model.Medicine;
import com.example.bluemedix_backend.model.Category;
import com.example.bluemedix_backend.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Page<Medicine> findByCategory(Category category, Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE " +
           "LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Medicine> search(String query, Pageable pageable);
    
    List<Medicine> findTop10ByOrderByCreatedAtDesc();
}

