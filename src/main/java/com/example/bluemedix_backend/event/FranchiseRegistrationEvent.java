package com.example.bluemedix_backend.event;


// import com.bluemedix.api.model.Franchise;
// import com.bluemedix.api.model.FranchiseStatus;
import com.example.bluemedix_backend.model.Franchise;
import com.example.bluemedix_backend.model.FranchiseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseRegistrationEvent {
    private Long franchiseId;
    private Long userId;
    private String businessName;
    private FranchiseStatus status;
    private LocalDateTime timestamp;
    private String eventType; // REGISTERED, APPROVED, REJECTED, etc.

    public static FranchiseRegistrationEvent fromFranchise(Franchise franchise, String eventType) {
        return new FranchiseRegistrationEvent(
            franchise.getId(),
            franchise.getUser().getId(),
            franchise.getShopName(),
            franchise.getStatus(),
            LocalDateTime.now(),
            eventType
        );
    }
}


