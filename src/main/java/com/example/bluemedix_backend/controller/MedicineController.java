package com.example.bluemedix_backend.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// import com.bluemedix.api.model.Category;
// import com.bluemedix.api.model.Medicine;
// import com.bluemedix.api.repository.CategoryRepository;
// import com.bluemedix.api.repository.MedicineRepository;
import com.example.bluemedix_backend.model.Category;
import com.example.bluemedix_backend.model.Medicine;
import com.example.bluemedix_backend.repository.CategoryRepository;
import com.example.bluemedix_backend.repository.MedicineRepository;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/medicines")
public class MedicineController {
    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public Page<Medicine> getAllMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        return medicineRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Medicine not found."));
        
        return ResponseEntity.ok(medicine);
    }

    @GetMapping("/category/{categoryId}")
    public Page<Medicine> getMedicinesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        return medicineRepository.findByCategory(category, pageable);
    }

    @GetMapping("/search")
    public Page<Medicine> searchMedicines(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return medicineRepository.search(query, pageable);
    }

    @GetMapping("/latest")
    public List<Medicine> getLatestMedicines() {
        return medicineRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Medicine createMedicine(@RequestBody Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicineDetails) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Medicine not found."));
        
        medicine.setName(medicineDetails.getName());
        medicine.setDescription(medicineDetails.getDescription());
        medicine.setPrice(medicineDetails.getPrice());
        medicine.setDiscountPrice(medicineDetails.getDiscountPrice());
        medicine.setImageUrl(medicineDetails.getImageUrl());
        medicine.setStock(medicineDetails.getStock());
        medicine.setPrescriptionRequired(medicineDetails.isPrescriptionRequired());
        medicine.setCategory(medicineDetails.getCategory());
        medicine.setTags(medicineDetails.getTags());
        medicine.setManufacturer(medicineDetails.getManufacturer());
        
        Medicine updatedMedicine = medicineRepository.save(medicine);
        return ResponseEntity.ok(updatedMedicine);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        medicineRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}


