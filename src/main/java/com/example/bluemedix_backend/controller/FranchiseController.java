package com.example.bluemedix_backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// import com.bluemedix.api.model.ERole;
// import com.bluemedix.api.model.Franchise;
// import com.bluemedix.api.model.FranchiseStatus;
// import com.bluemedix.api.model.Role;
// import com.bluemedix.api.model.User;
// import com.bluemedix.api.payload.request.FranchiseRequest;
// import com.bluemedix.api.payload.response.MessageResponse;
// import com.bluemedix.api.repository.FranchiseRepository;
// import com.bluemedix.api.repository.RoleRepository;
// import com.bluemedix.api.repository.UserRepository;
import com.example.bluemedix_backend.model.ERole;
import com.example.bluemedix_backend.model.Franchise;
import com.example.bluemedix_backend.model.FranchiseStatus;
import com.example.bluemedix_backend.model.Role;
import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.payload.request.FranchiseRequest;
import com.example.bluemedix_backend.payload.response.MessageResponse;
import com.example.bluemedix_backend.repository.FranchiseRepository;
import com.example.bluemedix_backend.repository.RoleRepository;
import com.example.bluemedix_backend.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/franchises")
public class FranchiseController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FranchiseRepository franchiseRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerFranchise(@Valid @RequestBody FranchiseRequest franchiseRequest) {
        if (userRepository.existsByEmail(franchiseRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user account
        User user = new User();
        user.setFirstName(franchiseRequest.getOwnerName().split(" ")[0]);
        user.setLastName(franchiseRequest.getOwnerName().contains(" ") ? 
                franchiseRequest.getOwnerName().substring(franchiseRequest.getOwnerName().indexOf(" ") + 1) : "");
        user.setEmail(franchiseRequest.getEmail());
        user.setPhone(franchiseRequest.getPhone());
        user.setPassword(encoder.encode(franchiseRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role franchiseRole = roleRepository.findByName(ERole.ROLE_FRANCHISE)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(franchiseRole);
        user.setRoles(roles);
        
        userRepository.save(user);

        // Create franchise record
        Franchise franchise = new Franchise();
        franchise.setUser(user);
        franchise.setShopName(franchiseRequest.getShopName());
        franchise.setOwnerName(franchiseRequest.getOwnerName());
        franchise.setAddress(franchiseRequest.getAddress());
        franchise.setCity(franchiseRequest.getCity());
        franchise.setState(franchiseRequest.getState());
        franchise.setPincode(franchiseRequest.getPincode());
        franchise.setLicenseNumber(franchiseRequest.getLicenseNumber());
        franchise.setGstNumber(franchiseRequest.getGstNumber());
        franchise.setStatus(FranchiseStatus.PENDING);
        
        franchiseRepository.save(franchise);

        return ResponseEntity.ok(new MessageResponse("Franchise registered successfully! Awaiting approval."));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Franchise> getPendingFranchises() {
        return franchiseRepository.findByStatus(FranchiseStatus.PENDING);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveFranchise(@PathVariable Long id) {
        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Franchise not found."));
        
        franchise.setStatus(FranchiseStatus.APPROVED);
        franchiseRepository.save(franchise);
        
        return ResponseEntity.ok(new MessageResponse("Franchise approved successfully!"));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectFranchise(@PathVariable Long id) {
        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Franchise not found."));
        
        franchise.setStatus(FranchiseStatus.REJECTED);
        franchiseRepository.save(franchise);
        
        return ResponseEntity.ok(new MessageResponse("Franchise rejected."));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Franchise> getAllFranchises() {
        return franchiseRepository.findAll();
    }
}


