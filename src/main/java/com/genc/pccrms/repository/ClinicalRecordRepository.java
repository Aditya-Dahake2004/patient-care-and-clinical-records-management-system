package com.genc.pccrms.repository;

import com.genc.pccrms.model.ClinicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, Integer> {

    // Get all records for a specific patient
    List<ClinicalRecord> findByPatient_PatientId(Integer patientId);

    // Get records by diagnosis code
    List<ClinicalRecord> findByDiagnosisCodeContainingIgnoreCase(String diagnosisCode);
}