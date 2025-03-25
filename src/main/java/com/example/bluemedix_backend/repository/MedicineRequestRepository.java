package com.example.bluemedix_backend.repository;



// import com.bluemedix.api.model.MedicineRequest;
// import com.bluemedix.api.model.RequestStatus;
// import com.bluemedix.api.model.User;
import com.example.bluemedix_backend.model.MedicineRequest;
import com.example.bluemedix_backend.model.RequestStatus;
import com.example.bluemedix_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRequestRepository extends JpaRepository<MedicineRequest, Long> {
    List<MedicineRequest> findByUser(User user);
    // List<MedicineRequest> findByStatus(RequestStatus status);  findByUser(User user);
    List<MedicineRequest> findByStatus(RequestStatus status);
}




