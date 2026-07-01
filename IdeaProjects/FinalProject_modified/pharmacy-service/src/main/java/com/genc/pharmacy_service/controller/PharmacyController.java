package com.genc.pharmacy_service.controller;

import com.genc.pharmacy_service.dto.*;
import com.genc.pharmacy_service.model.DispenseStatus;
import com.genc.pharmacy_service.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
@Slf4j
public class PharmacyController {

    private final PharmacyService pharmacyService;

    // ==================== PRESCRIPTION ENDPOINTS ====================

    // Create prescription
    @PostMapping("/prescriptions")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> createPrescription(
            @Valid @RequestBody PrescriptionRequest request) {
        try {
            PrescriptionDTO prescription = pharmacyService.createPrescription(request);
            return ResponseEntity.ok(ApiResponse.success("Prescription created successfully", prescription));
        } catch (Exception e) {
            log.error("Error creating prescription: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Dispense medication
    @PutMapping("/prescriptions/{prescriptionId}/dispense")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> dispenseMedication(
            @PathVariable Long prescriptionId,
            @RequestParam Long pharmacistId) {
        try {
            PrescriptionDTO prescription = pharmacyService.dispenseMedication(prescriptionId, pharmacistId);
            return ResponseEntity.ok(ApiResponse.success("Medication dispensed successfully", prescription));
        } catch (Exception e) {
            log.error("Error dispensing medication: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Cancel prescription
    @PutMapping("/prescriptions/{prescriptionId}/cancel")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> cancelPrescription(
            @PathVariable Long prescriptionId) {
        try {
            PrescriptionDTO prescription = pharmacyService.cancelPrescription(prescriptionId);
            return ResponseEntity.ok(ApiResponse.success("Prescription cancelled", prescription));
        } catch (Exception e) {
            log.error("Error cancelling prescription: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Get prescription by ID
    @GetMapping("/prescriptions/{prescriptionId}")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> getPrescriptionById(
            @PathVariable Long prescriptionId) {
        try {
            PrescriptionDTO prescription = pharmacyService.getPrescriptionById(prescriptionId);
            return ResponseEntity.ok(ApiResponse.success(prescription));
        } catch (Exception e) {
            log.error("Error fetching prescription: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Get patient prescriptions
    @GetMapping("/prescriptions/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getPatientPrescriptions(
            @PathVariable Long patientId) {
        List<PrescriptionDTO> prescriptions = pharmacyService.getPatientPrescriptions(patientId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    // Get pending prescriptions
    @GetMapping("/prescriptions/pending")
    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getPendingPrescriptions() {
        List<PrescriptionDTO> prescriptions = pharmacyService.getPendingPrescriptions();
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    // Get all prescriptions
    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getAllPrescriptions() {
        List<PrescriptionDTO> prescriptions = pharmacyService.getAllPrescriptions();
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    // Get prescriptions by status
    @GetMapping("/prescriptions/status/{status}")
    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getPrescriptionsByStatus(
            @PathVariable DispenseStatus status) {
        List<PrescriptionDTO> prescriptions = pharmacyService.getPrescriptionsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    // ==================== DRUG INTERACTION ENDPOINTS ====================

    // Check drug interactions
    @PostMapping("/interactions/check")
    public ResponseEntity<ApiResponse<List<DrugInteractionDTO>>> checkDrugInteractions(
            @RequestBody List<String> drugNames) {
        List<DrugInteractionDTO> interactions = pharmacyService.checkDrugInteractions(drugNames);
        String message = interactions.isEmpty() ? "No interactions found" : 
                "Found " + interactions.size() + " potential interaction(s)";
        return ResponseEntity.ok(ApiResponse.success(message, interactions));
    }

    // Get interactions for a drug
    @GetMapping("/interactions/{drugName}")
    public ResponseEntity<ApiResponse<List<DrugInteractionDTO>>> getDrugInteractions(
            @PathVariable String drugName) {
        List<DrugInteractionDTO> interactions = pharmacyService.getDrugInteractions(drugName);
        return ResponseEntity.ok(ApiResponse.success(interactions));
    }

    // ==================== MEDICATION/INVENTORY ENDPOINTS ====================

    // Add medication to inventory
    @PostMapping("/medications")
    public ResponseEntity<ApiResponse<MedicationDTO>> addMedication(
            @Valid @RequestBody MedicationRequest request) {
        try {
            MedicationDTO medication = pharmacyService.addMedication(request);
            return ResponseEntity.ok(ApiResponse.success("Medication added successfully", medication));
        } catch (Exception e) {
            log.error("Error adding medication: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Update medication stock
    @PutMapping("/medications/{medicationId}/stock")
    public ResponseEntity<ApiResponse<MedicationDTO>> updateStock(
            @PathVariable Long medicationId,
            @RequestParam Integer quantity) {
        try {
            MedicationDTO medication = pharmacyService.updateStock(medicationId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Stock updated successfully", medication));
        } catch (Exception e) {
            log.error("Error updating stock: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Get medication by ID
    @GetMapping("/medications/{medicationId}")
    public ResponseEntity<ApiResponse<MedicationDTO>> getMedicationById(
            @PathVariable Long medicationId) {
        try {
            MedicationDTO medication = pharmacyService.getMedicationById(medicationId);
            return ResponseEntity.ok(ApiResponse.success(medication));
        } catch (Exception e) {
            log.error("Error fetching medication: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Search medications
    @GetMapping("/medications/search")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> searchMedications(
            @RequestParam String name) {
        List<MedicationDTO> medications = pharmacyService.searchMedications(name);
        return ResponseEntity.ok(ApiResponse.success(medications));
    }

    // Get all medications
    @GetMapping("/medications")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> getAllMedications() {
        List<MedicationDTO> medications = pharmacyService.getAllMedications();
        return ResponseEntity.ok(ApiResponse.success(medications));
    }

    // Get low stock medications
    @GetMapping("/medications/low-stock")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> getLowStockMedications() {
        List<MedicationDTO> medications = pharmacyService.getLowStockMedications();
        return ResponseEntity.ok(ApiResponse.success(medications));
    }

    // Get expiring medications
    @GetMapping("/medications/expiring")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> getExpiringMedications(
            @RequestParam(defaultValue = "30") int days) {
        List<MedicationDTO> medications = pharmacyService.getExpiringMedications(days);
        return ResponseEntity.ok(ApiResponse.success(medications));
    }
}

