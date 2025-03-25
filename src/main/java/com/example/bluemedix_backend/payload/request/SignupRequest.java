package com.example.bluemedix_backend.payload.request;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
// import lombok.Data;

// import java.util.Set;

// @Data
// public class SignupRequest {
//     @NotBlank
//     @Size(min = 2, max = 50)
//     private String firstName;

//     @NotBlank
//     @Size(min = 2, max = 50)
//     private String lastName;

//     @NotBlank
//     @Size(max = 50)
//     @Email
//     private String email;

//     @Size(max = 20)
//     private String phone;
 
//     @NotBlank
//     @Size(min = 6, max = 40)
//     private String password;
    
//     private Set<String> roles;
// }


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
  private String username;

  @NotBlank
  @Size(min = 2, max = 50)
  private String firstName;

  @NotBlank
  @Size(min = 2, max = 50)
  private String lastName;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @Size(max = 20)
  private String phone;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
  
  private Set<String> roles;
}


