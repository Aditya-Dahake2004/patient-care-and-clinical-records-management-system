package com.genc.pccrms.service;

import com.genc.pccrms.model.Patient;
import com.genc.pccrms.repository.AppointmentRepository;
import com.genc.pccrms.repository.ClinicalRecordRepository;
import com.genc.pccrms.repository.InvoiceRepository;
import com.genc.pccrms.repository.PatientRepository;
import com.genc.pccrms.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClinicalRecordRepository clinicalRecordRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

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

    @Transactional
    public void deletePatient(Integer patientId) {
        appointmentRepository.deleteAll(appointmentRepository.findByPatient_PatientId(patientId));
        prescriptionRepository.deleteAll(prescriptionRepository.findByPatient_PatientId(patientId));
        clinicalRecordRepository.deleteAll(clinicalRecordRepository.findByPatient_PatientId(patientId));
        invoiceRepository.deleteAll(invoiceRepository.findByPatient_PatientId(patientId));
        patientRepository.deleteById(patientId);
    }

    private String generateMRN() {
        long count = patientRepository.count() + 1;
        return String.format("MRN-%05d", count);
    }
}