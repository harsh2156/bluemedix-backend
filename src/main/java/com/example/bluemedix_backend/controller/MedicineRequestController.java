package com.example.bluemedix_backend.controller;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// import com.bluemedix.api.model.MedicineRequest;
// import com.bluemedix.api.model.RequestStatus;
// import com.bluemedix.api.model.User;
// import com.bluemedix.api.repository.MedicineRequestRepository;
// import com.bluemedix.api.repository.UserRepository;

import com.example.bluemedix_backend.model.MedicineRequest;
import com.example.bluemedix_backend.model.RequestStatus;
import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.repository.MedicineRequestRepository;
import com.example.bluemedix_backend.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/medicine-requests")
public class MedicineRequestController {
    @Autowired
    MedicineRequestRepository medicineRequestRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('USER')")
    public List<MedicineRequest> getMyRequests(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        
        return medicineRequestRepository.findByUser(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public MedicineRequest createRequest(@RequestBody MedicineRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        
        return medicineRequestRepository.save(request);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicineRequest> getAllRequests() {
        return medicineRequestRepository.findAll();
    }

    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicineRequest> getRequestsByStatus(@PathVariable String status) {
        RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
        return medicineRequestRepository.findByStatus(requestStatus);
    }

    @PutMapping("/{id}/update-status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicineRequest> updateRequestStatus(
            @PathVariable Long id, 
            @PathVariable String status,
            @RequestBody(required = false) String adminNotes) {
        
        MedicineRequest request = medicineRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Medicine request not found."));
        
        RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
        request.setStatus(requestStatus);
        
        if (adminNotes != null && !adminNotes.isEmpty()) {
            request.setAdminNotes(adminNotes);
        }
        
        MedicineRequest updatedRequest = medicineRequestRepository.save(request);
        return ResponseEntity.ok(updatedRequest);
    }
}


