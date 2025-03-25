package com.example.bluemedix_backend.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSNotification {
    private String phoneNumber;
    private String message;
}

