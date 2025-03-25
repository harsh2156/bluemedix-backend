package com.example.bluemedix_backend.controller;


import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// import com.bluemedix.api.model.Prescription;
// import com.bluemedix.api.model.User;
// import com.bluemedix.api.repository.PrescriptionRepository;
// import com.bluemedix.api.repository.UserRepository;
// import com.bluemedix.api.service.FileStorageService;
import com.example.bluemedix_backend.model.Prescription;
import com.example.bluemedix_backend.model.User;
import com.example.bluemedix_backend.repository.PrescriptionRepository;
import com.example.bluemedix_backend.repository.UserRepository;
import com.example.bluemedix_backend.service.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @PostMapping("/upload/prescription")
    @PreAuthorize("hasRole('USER')")
    public Prescription uploadPrescription(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "doctorName", required = false) String doctorName,
            Principal principal) {
        
        String fileName = fileStorageService.storeFile(file);
        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/download/")
                .path(fileName)
                .toUriString();
        
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        
        Prescription prescription = new Prescription();
        prescription.setUser(user);
        prescription.setFileUrl(fileDownloadUri);
        prescription.setDoctorName(doctorName);
        
        return prescriptionRepository.save(prescription);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Default content type
        }
        
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/prescriptions")
    @PreAuthorize("hasRole('USER')")
    public List<Prescription> getMyPrescriptions(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        
        return prescriptionRepository.findByUser(user);
    }

    @GetMapping("/admin/prescriptions")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
}


