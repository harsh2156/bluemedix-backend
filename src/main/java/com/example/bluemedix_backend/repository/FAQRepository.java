package com.example.bluemedix_backend.repository;


import com.example.bluemedix_backend.model.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByActiveOrderByDisplayOrderAsc(boolean active);
}

