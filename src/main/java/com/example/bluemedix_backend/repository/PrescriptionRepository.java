package com.example.bluemedix_backend.repository;

// import com.bluemedix.api.model.Prescription;
// import com.bluemedix.api.model.PrescriptionStatus;
// import com.bluemedix.api.model.User;
import com.example.bluemedix_backend.model.Prescription;
import com.example.bluemedix_backend.model.PrescriptionStatus;
import com.example.bluemedix_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByUser(User user);
    List<Prescription> findByStatus(PrescriptionStatus status);
}


