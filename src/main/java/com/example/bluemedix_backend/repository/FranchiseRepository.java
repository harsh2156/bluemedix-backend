package com.example.bluemedix_backend.repository;


// import com.bluemedix.api.model.Franchise;
// import com.bluemedix.api.model.FranchiseStatus;
// import com.bluemedix.api.model.User;
import com.example.bluemedix_backend.model.Franchise;
import com.example.bluemedix_backend.model.FranchiseStatus;
import com.example.bluemedix_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {
    Optional<Franchise> findByUser(User user);
    List<Franchise> findByStatus(FranchiseStatus status);
}


