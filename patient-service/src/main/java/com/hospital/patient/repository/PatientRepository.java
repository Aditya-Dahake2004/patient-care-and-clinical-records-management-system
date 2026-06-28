package com.hospital.patient.repository;

import com.hospital.patient.model.Patient;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByMrn(String mrn);

    boolean existsByFullNameIgnoreCaseAndDateOfBirth(String fullName, LocalDate dateOfBirth);

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.mrn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "p.contactNumber LIKE CONCAT('%', :query, '%')")
    List<Patient> searchPatients(@Param("query") String query);
}

