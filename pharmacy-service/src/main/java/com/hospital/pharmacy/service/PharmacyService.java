package com.hospital.pharmacy.service;

import com.hospital.pharmacy.dto.InteractionResult;
import com.hospital.pharmacy.dto.PrescriptionRequest;
import com.hospital.pharmacy.model.DispenseStatus;
import com.hospital.pharmacy.model.Prescription;
import com.hospital.pharmacy.repository.PrescriptionRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PharmacyService {

    private final PrescriptionRepository prescriptionRepository;

    // Mock drug interaction database
    private static final Map<Set<String>, String[]> INTERACTIONS = Map.of(
        Set.of("lisinopril", "potassium"), new String[]{"CRITICAL", "Critical: Concurrent use of ACE inhibitors with potassium supplements significantly increases risk of hyperkalemia."},
        Set.of("warfarin", "aspirin"), new String[]{"CRITICAL", "Critical: Combined use increases risk of serious bleeding events."},
        Set.of("metformin", "alcohol"), new String[]{"CRITICAL", "Critical: Alcohol combined with Metformin increases risk of lactic acidosis."},
        Set.of("lisinopril", "ibuprofen"), new String[]{"MODERATE", "Moderate: NSAIDs may reduce the antihypertensive effect of ACE inhibitors and increase renal risk."},
        Set.of("atorvastatin", "grapefruit"), new String[]{"MODERATE", "Moderate: Grapefruit increases statin levels, potentially increasing risk of muscle-related side effects."},
        Set.of("metformin", "contrast"), new String[]{"MODERATE", "Moderate: IV contrast media with Metformin may increase risk of renal impairment."}
    );

    public PharmacyService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Transactional
    public Prescription createPrescription(PrescriptionRequest request) {
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (request.getMedicationName() == null || request.getMedicationName().isBlank()) {
            throw new IllegalArgumentException("Medication name is required");
        }

        Prescription rx = new Prescription();
        rx.setPatientId(request.getPatientId());
        rx.setMedicationName(request.getMedicationName().trim());
        rx.setDosage(request.getDosage() != null ? request.getDosage().trim() : "As directed");
        rx.setFrequency(request.getFrequency() != null ? request.getFrequency().trim() : "As directed");
        rx.setDispenseStatus(DispenseStatus.PENDING);

        return prescriptionRepository.save(rx);
    }

    @Transactional
    public Prescription dispensePrescription(Long prescriptionId) {
        Prescription rx = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with ID: " + prescriptionId));

        if (rx.getDispenseStatus() == DispenseStatus.DISPENSED) {
            throw new IllegalStateException("This prescription has already been dispensed");
        }

        rx.setDispenseStatus(DispenseStatus.DISPENSED);
        return prescriptionRepository.save(rx);
    }

    public List<Prescription> getPendingPrescriptions() {
        return prescriptionRepository.findByDispenseStatusOrderByPrescriptionIdDesc(DispenseStatus.PENDING);
    }

    public List<Prescription> getPatientPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatientIdOrderByPrescriptionIdDesc(patientId);
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public InteractionResult checkInteractions(String drugA, String drugB) {
        String a = drugA.toLowerCase().trim();
        String b = drugB.toLowerCase().trim();

        for (Map.Entry<Set<String>, String[]> entry : INTERACTIONS.entrySet()) {
            Set<String> pair = entry.getKey();
            boolean match = pair.stream().anyMatch(a::contains) && pair.stream().anyMatch(b::contains)
                    && pair.stream().filter(k -> a.contains(k) || b.contains(k)).count() >= 2;

            if (match) {
                String[] info = entry.getValue();
                return new InteractionResult(drugA, drugB, info[0], info[1], false);
            }
        }

        return new InteractionResult(drugA, drugB, "NONE",
                "No known clinically significant interactions found between " + drugA + " and " + drugB + ".", true);
    }
}

