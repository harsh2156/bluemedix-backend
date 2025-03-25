// package com.example.bluemedix_backend.controller;



// // import com.bluemedix.api.annotation.AuthRateLimited;
// // import com.bluemedix.api.event.UserRegistrationEvent;
// // import com.bluemedix.api.message.EmailNotification;
// // import com.bluemedix.api.model.ERole;
// // import com.bluemedix.api.model.Role;
// // import com.bluemedix.api.model.User;
// import com.example.bluemedix_backend.annotation.AuthRateLimited;
// import com.example.bluemedix_backend.model.ERole;
// import com.example.bluemedix_backend.model.Role;
// import com.example.bluemedix_backend.model.User;
// import com.example.bluemedix_backend.event.UserRegistrationEvent;
// import com.example.bluemedix_backend.message.EmailNotification;

// // import com.bluemedix.api.payload.request.LoginRequest;
// // import com.bluemedix.api.payload.request.SignupRequest;
// // import com.bluemedix.api.payload.response.JwtResponse;
// // import com.bluemedix.api.payload.response.MessageResponse;
// import com.example.bluemedix_backend.payload.request.*;
// import com.example.bluemedix_backend.payload.response.*;

// // import com.bluemedix.api.repository.RoleRepository;
// // import com.bluemedix.api.repository.UserRepository;
// import com.example.bluemedix_backend.repository.RoleRepository;
// import com.example.bluemedix_backend.repository.UserRepository;

// // import com.bluemedix.api.security.jwt.JwtUtils;
// // import com.bluemedix.api.security.services.UserDetailsImpl;
// import com.example.bluemedix_backend.security.jwt.JwtUtils;
// import com.example.bluemedix_backend.security.services.UserDetailsImpl;

// // import com.bluemedix.api.service.kafka.KafkaProducerService;
// // import com.bluemedix.api.service.rabbitmq.RabbitMQProducerService;
// import com.example.bluemedix_backend.service.kafka.KafkaProducerService;
// import com.example.bluemedix_backend.service.rabbitmq.RabbitMQProducerService;

// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;
// import java.util.stream.Collectors;

// @CrossOrigin(origins = "*", maxAge = 3600)
// @RestController
// @RequestMapping("/auth")
// @RequiredArgsConstructor
// public class AuthController {
//     private final AuthenticationManager authenticationManager;
//     private final UserRepository userRepository;
//     private final RoleRepository roleRepository;
//     private final PasswordEncoder encoder;
//     private final JwtUtils jwtUtils;
//     private final KafkaProducerService kafkaProducerService;
//     private final RabbitMQProducerService rabbitMQProducerService;

//     @PostMapping("/signin")
//     @AuthRateLimited
//     public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//         Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

//         SecurityContextHolder.getContext().setAuthentication(authentication);
//         String jwt = jwtUtils.generateJwtToken(authentication);

//         UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//         List<String> roles = userDetails.getAuthorities().stream()
//                 .map(item -> item.getAuthority())
//                 .collect(Collectors.toList());

//         return ResponseEntity.ok(new JwtResponse(jwt,
//                 userDetails.getId(),
//                 userDetails.getUsername(),
//                 userDetails.getEmail(),
//                 roles));
//     }

//     @PostMapping("/signup")
//     @AuthRateLimited
//     public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//         if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//             return ResponseEntity
//                     .badRequest()
//                     .body(new MessageResponse("Error: Username is already taken!"));
//         }

//         if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//             return ResponseEntity
//                     .badRequest()
//                     .body(new MessageResponse("Error: Email is already in use!"));
//         }

//         // Create new user's account
//         User user = new User(signUpRequest.getUsername(),
//                 signUpRequest.getEmail(),
//                 encoder.encode(signUpRequest.getPassword()));

//         Set<String> strRoles = signUpRequest.getRoles();
//         Set<Role> roles = new HashSet<>();

//         if (strRoles == null) {
//             Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                     .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//             roles.add(userRole);
//         } else {
//             strRoles.forEach(role -> {
//                 switch (role) {
//                     case "admin":
//                         Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                 .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                         roles.add(adminRole);
//                         break;
//                     case "franchise":
//                         Role franchiseRole = roleRepository.findByName(ERole.ROLE_FRANCHISE)
//                                 .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                         roles.add(franchiseRole);
//                         break;
//                     case "warehouse":
//                         Role warehouseRole = roleRepository.findByName(ERole.ROLE_WAREHOUSE)
//                                 .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                         roles.add(warehouseRole);
//                         break;
//                     default:
//                         Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                 .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                         roles.add(userRole);
//                 }
//             });
//         }

//         user.setRoles(roles);
//         User savedUser = userRepository.save(user);

//         // Send Kafka event
//         UserRegistrationEvent event = UserRegistrationEvent.fromUser(savedUser, "REGISTERED");
//         kafkaProducerService.sendUserRegistrationEvent(event);

//         // Send welcome email via RabbitMQ
//         EmailNotification emailNotification = new EmailNotification(
//                 savedUser.getEmail(),
//                 "Welcome to BlueMedix",
//                 "Thank you for registering with BlueMedix!",
//                 "welcome-template",
//                 savedUser
//         );
//         rabbitMQProducerService.sendEmailNotification(emailNotification);

//         return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//     }
// }


package com.example.bluemedix_backend.controller;
// import com.bluemedix.api.annotation.AuthRateLimited;
// import com.bluemedix.api.event.UserRegistrationEvent;
// import com.bluemedix.api.message.EmailNotification;
// import com.bluemedix.api.model.ERole;
// import com.bluemedix.api.model.Role;
// import com.bluemedix.api.model.User;
import com.example.bluemedix_backend.annotation.AuthRateLimited;
import com.example.bluemedix_backend.model.ERole;
import com.example.bluemedix_backend.model.Role;
import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.event.UserRegistrationEvent;
import com.example.bluemedix_backend.message.EmailNotification;

// import com.bluemedix.api.payload.request.LoginRequest;
// import com.bluemedix.api.payload.request.SignupRequest;
// import com.bluemedix.api.payload.response.JwtResponse;
// import com.bluemedix.api.payload.response.MessageResponse;
import com.example.bluemedix_backend.payload.request.*;
import com.example.bluemedix_backend.payload.response.*;

// import com.bluemedix.api.repository.RoleRepository;
// import com.bluemedix.api.repository.UserRepository;
import com.example.bluemedix_backend.repository.RoleRepository;
import com.example.bluemedix_backend.repository.UserRepository;

// import com.bluemedix.api.security.jwt.JwtUtils;
// import com.bluemedix.api.security.services.UserDetailsImpl;
import com.example.bluemedix_backend.security.jwt.JwtUtils;
import com.example.bluemedix_backend.security.services.UserDetailsImpl;

// import com.bluemedix.api.service.kafka.KafkaProducerService;
// import com.bluemedix.api.service.rabbitmq.RabbitMQProducerService;
import com.example.bluemedix_backend.service.kafka.KafkaProducerService;
import com.example.bluemedix_backend.service.rabbitmq.RabbitMQProducerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final KafkaProducerService kafkaProducerService;
    private final RabbitMQProducerService rabbitMQProducerService;

    @PostMapping("/signin")
    @AuthRateLimited
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    @AuthRateLimited
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check if username is provided, if not use email as username
        String username = signUpRequest.getUsername();
        if (username == null || username.trim().isEmpty()) {
            username = signUpRequest.getEmail();
        }
        
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
            username,
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));
    
        // Set first name and last name
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
    
        // Set phone if provided
        if (signUpRequest.getPhone() != null) {
            user.setPhone(signUpRequest.getPhone());
        }

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "franchise":
                        Role franchiseRole = roleRepository.findByName(ERole.ROLE_FRANCHISE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(franchiseRole);
                        break;
                    case "warehouse":
                        Role warehouseRole = roleRepository.findByName(ERole.ROLE_WAREHOUSE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(warehouseRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        // Send Kafka event
        UserRegistrationEvent event = UserRegistrationEvent.fromUser(savedUser, "REGISTERED");
        kafkaProducerService.sendUserRegistrationEvent(event);

        // Send welcome email via RabbitMQ
        EmailNotification emailNotification = new EmailNotification(
            savedUser.getEmail(),
            "Welcome to BlueMedix",
            "Thank you for registering with BlueMedix!",
            "welcome-template",
            savedUser
        );
        rabbitMQProducerService.sendEmailNotification(emailNotification);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}

