package com.example.bluemedix_backend.event;

// import com.bluemedix.api.model.Prescription;
// import com.bluemedix.api.model.PrescriptionStatus;
import com.example.bluemedix_backend.model.Prescription;
import com.example.bluemedix_backend.model.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionEvent {
    private Long prescriptionId;
    private Long userId;
    private PrescriptionStatus status;
    private LocalDateTime timestamp;
    private String eventType; // UPLOADED, PROCESSED, etc.

    public static PrescriptionEvent fromPrescription(Prescription prescription, String eventType) {
        return new PrescriptionEvent(
            prescription.getId(),
            prescription.getUser().getId(),
            prescription.getStatus(),
            LocalDateTime.now(),
            eventType
        );
    }
}


