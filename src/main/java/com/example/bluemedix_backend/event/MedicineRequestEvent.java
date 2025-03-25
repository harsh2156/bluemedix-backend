package com.example.bluemedix_backend.event;


// import com.bluemedix.api.model.MedicineRequest;
// import com.bluemedix.api.model.RequestStatus;
import com.example.bluemedix_backend.model.MedicineRequest;
import com.example.bluemedix_backend.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequestEvent {
    private Long requestId;
    private Long userId;
    private RequestStatus status;
    private LocalDateTime timestamp;
    private String eventType; // CREATED, APPROVED, REJECTED, etc.

    public static MedicineRequestEvent fromMedicineRequest(MedicineRequest request, String eventType) {
        return new MedicineRequestEvent(
            request.getId(),
            request.getUser().getId(),
            request.getStatus(),
            LocalDateTime.now(),
            eventType
        );
    }
}


