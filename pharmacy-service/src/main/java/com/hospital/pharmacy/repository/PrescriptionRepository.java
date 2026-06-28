package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.model.DispenseStatus;
import com.hospital.pharmacy.model.Prescription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientIdOrderByPrescriptionIdDesc(Long patientId);

    List<Prescription> findByDispenseStatusOrderByPrescriptionIdDesc(DispenseStatus status);
}

