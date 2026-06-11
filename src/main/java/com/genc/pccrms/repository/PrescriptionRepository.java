package com.genc.pccrms.repository;

import com.genc.pccrms.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    // Get all prescriptions for a specific patient
    List<Prescription> findByPatient_PatientId(Integer patientId);

    // Get prescriptions by dispense status
    List<Prescription> findByDispenseStatus(Prescription.DispenseStatus status);
}