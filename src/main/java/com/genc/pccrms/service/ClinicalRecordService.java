package com.genc.pccrms.service;

import com.genc.pccrms.model.ClinicalRecord;
import com.genc.pccrms.model.Patient;
import com.genc.pccrms.repository.ClinicalRecordRepository;
import com.genc.pccrms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalRecordService {

    @Autowired
    private ClinicalRecordRepository clinicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    public ClinicalRecord createRecord(Integer patientId, ClinicalRecord record) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        record.setPatient(patient);
        return clinicalRecordRepository.save(record);
    }

    public List<ClinicalRecord> getAllRecords() {
        return clinicalRecordRepository.findAll();
    }
    public Optional<ClinicalRecord> getRecordById(Integer id) {
        return clinicalRecordRepository.findById(id);
    }

    public List<ClinicalRecord> getRecordsByPatient(Integer patientId) {
        return clinicalRecordRepository.findByPatient_PatientId(patientId);
    }

    public ClinicalRecord updateRecord(Integer id, ClinicalRecord updated, Integer patientId) {
        ClinicalRecord existing = clinicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setPatient(patient);
        existing.setEncounterDate(updated.getEncounterDate());
        existing.setDiagnosisCode(updated.getDiagnosisCode());
        existing.setClinicalNotes(updated.getClinicalNotes());
        existing.setVitalsSummary(updated.getVitalsSummary());
        return clinicalRecordRepository.save(existing);
    }

    // Delete record
    public void deleteRecord(Integer id) {
        clinicalRecordRepository.deleteById(id);
    }
}