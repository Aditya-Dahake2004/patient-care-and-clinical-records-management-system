package com.hospital.pharmacy.controller;

import com.hospital.pharmacy.dto.InteractionResult;
import com.hospital.pharmacy.dto.PrescriptionRequest;
import com.hospital.pharmacy.model.Prescription;
import com.hospital.pharmacy.service.PharmacyService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @PostMapping("/prescription")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequest request) {
        try {
            Prescription rx = pharmacyService.createPrescription(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(rx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/prescription/pending")
    public ResponseEntity<List<Prescription>> getPendingPrescriptions() {
        return ResponseEntity.ok(pharmacyService.getPendingPrescriptions());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@PathVariable("patientId") Long patientId) {
        return ResponseEntity.ok(pharmacyService.getPatientPrescriptions(patientId));
    }

    @GetMapping("/prescription/{id}")
    public ResponseEntity<?> getPrescription(@PathVariable("id") Long id) {
        Optional<Prescription> rx = pharmacyService.getPrescriptionById(id);
        if (rx.isPresent()) {
            return ResponseEntity.ok(rx.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Prescription not found with ID: " + id));
    }

    @PutMapping("/prescription/{id}/dispense")
    public ResponseEntity<?> dispensePrescription(@PathVariable("id") Long id) {
        try {
            Prescription rx = pharmacyService.dispensePrescription(id);
            return ResponseEntity.ok(rx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/check-interactions")
    public ResponseEntity<InteractionResult> checkInteractions(
            @RequestParam("drugA") String drugA,
            @RequestParam("drugB") String drugB) {
        InteractionResult result = pharmacyService.checkInteractions(drugA, drugB);
        return ResponseEntity.ok(result);
    }
}

