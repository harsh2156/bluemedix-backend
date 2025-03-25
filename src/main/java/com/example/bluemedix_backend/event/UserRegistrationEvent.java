package com.example.bluemedix_backend.event;


import com.example.bluemedix_backend.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationEvent {
    private Long userId;
    private String email;
    private List<String> roles;
    private LocalDateTime timestamp;
    private String eventType;
    
    public static UserRegistrationEvent fromUser(User user, String eventType) {
        return new UserRegistrationEvent(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()),
                LocalDateTime.now(),
                eventType
        );
    }
}

