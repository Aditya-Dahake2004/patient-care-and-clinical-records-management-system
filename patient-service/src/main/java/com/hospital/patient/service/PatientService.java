package com.hospital.patient.service;

import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.model.Patient;
import com.hospital.patient.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional
    public Patient registerPatient(PatientRequest request) {
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (request.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (patientRepository.existsByFullNameIgnoreCaseAndDateOfBirth(
                request.getFullName().trim(), request.getDateOfBirth())) {
            throw new IllegalStateException("A patient with the same name and date of birth already exists");
        }

        Patient patient = new Patient();
        patient.setMrn(generateMrn());
        patient.setFullName(request.getFullName().trim());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender() != null ? request.getGender().trim() : null);
        patient.setContactNumber(request.getContactNumber() != null ? request.getContactNumber().trim() : null);

        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long patientId, PatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            patient.setFullName(request.getFullName().trim());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender().trim());
        }
        if (request.getContactNumber() != null) {
            patient.setContactNumber(request.getContactNumber().trim());
        }

        return patientRepository.save(patient);
    }

    public Optional<Patient> getPatientById(Long patientId) {
        return patientRepository.findById(patientId);
    }

    public Optional<Patient> getPatientByMrn(String mrn) {
        return patientRepository.findByMrn(mrn);
    }

    public List<Patient> searchPatients(String query) {
        if (query == null || query.isBlank()) {
            return patientRepository.findAll();
        }
        return patientRepository.searchPatients(query.trim());
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    private String generateMrn() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "MRN-" + uuid;
    }
}

