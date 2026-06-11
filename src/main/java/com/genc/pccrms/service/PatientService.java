package com.genc.pccrms.service;

import com.genc.pccrms.model.Patient;
import com.genc.pccrms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient registerPatient(Patient patient) {
        String mrn = generateMRN();
        patient.setMrn(mrn);
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Integer patientId, Patient updatedPatient) {
        Patient existing = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setFullName(updatedPatient.getFullName());
        existing.setDateOfBirth(updatedPatient.getDateOfBirth());
        existing.setGender(updatedPatient.getGender());
        existing.setContactNumber(updatedPatient.getContactNumber());
        return patientRepository.save(existing);
    }

    public Optional<Patient> getPatientById(Integer patientId) {
        return patientRepository.findById(patientId);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> searchPatients(String keyword) {
        return patientRepository.searchPatients(keyword);
    }

    public void deletePatient(Integer patientId) {
        patientRepository.deleteById(patientId);
    }

    private String generateMRN() {
        long count = patientRepository.count() + 1;
        return String.format("MRN-%05d", count);
    }
}