package com.example.bluemedix_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// import com.bluemedix.api.model.FAQ;
// import com.bluemedix.api.repository.FAQRepository;
import com.example.bluemedix_backend.model.FAQ;
import com.example.bluemedix_backend.repository.FAQRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/faqs")
public class FAQController {
    @Autowired
    FAQRepository faqRepository;

    @GetMapping
    public List<FAQ> getActiveFAQs() {
        return faqRepository.findByActiveOrderByDisplayOrderAsc(true);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FAQ> getAllFAQs() {
        return faqRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public FAQ createFAQ(@RequestBody FAQ faq) {
        return faqRepository.save(faq);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FAQ> updateFAQ(@PathVariable Long id, @RequestBody FAQ faqDetails) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: FAQ not found."));
        
        faq.setQuestion(faqDetails.getQuestion());
        faq.setAnswer(faqDetails.getAnswer());
        faq.setDisplayOrder(faqDetails.getDisplayOrder());
        faq.setActive(faqDetails.isActive());
        
        FAQ updatedFAQ = faqRepository.save(faq);
        return ResponseEntity.ok(updatedFAQ);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFAQ(@PathVariable Long id) {
        faqRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

