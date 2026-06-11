package com.genc.pccrms.service;

import com.genc.pccrms.model.Patient;
import com.genc.pccrms.model.Prescription;
import com.genc.pccrms.repository.PatientRepository;
import com.genc.pccrms.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Create new prescription
    public Prescription createPrescription(Integer patientId, Prescription prescription) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        prescription.setPatient(patient);
        prescription.setDispenseStatus(Prescription.DispenseStatus.PENDING);
        return prescriptionRepository.save(prescription);
    }

    // Get all prescriptions
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Get prescription by ID
    public Optional<Prescription> getPrescriptionById(Integer id) {
        return prescriptionRepository.findById(id);
    }

    // Get prescriptions by patient
    public List<Prescription> getPrescriptionsByPatient(Integer patientId) {
        return prescriptionRepository.findByPatient_PatientId(patientId);
    }

    // Update prescription
    public Prescription updatePrescription(Integer id, Prescription updated, Integer patientId) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setPatient(patient);
        existing.setMedicationName(updated.getMedicationName());
        existing.setDosage(updated.getDosage());
        existing.setFrequency(updated.getFrequency());
        existing.setDispenseStatus(updated.getDispenseStatus());
        return prescriptionRepository.save(existing);
    }

    // Delete prescription
    public void deletePrescription(Integer id) {
        prescriptionRepository.deleteById(id);
    }

    // Dispense medication - just update status to DISPENSED
    public Prescription dispenseMedication(Integer id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescription.setDispenseStatus(Prescription.DispenseStatus.DISPENSED);
        return prescriptionRepository.save(prescription);
    }
}