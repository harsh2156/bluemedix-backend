package com.example.bluemedix_backend.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FranchiseRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String shopName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String ownerName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 50)
    private String city;

    @NotBlank
    @Size(max = 50)
    private String state;

    @NotBlank
    @Size(max = 10)
    private String pincode;

    @NotBlank
    @Size(max = 50)
    private String licenseNumber;

    @Size(max = 50)
    private String gstNumber;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}


