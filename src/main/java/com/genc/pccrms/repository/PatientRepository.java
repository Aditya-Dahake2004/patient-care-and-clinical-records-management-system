package com.genc.pccrms.repository;


import com.genc.pccrms.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // Find patient by MRN
    Optional<Patient> findByMrn(String mrn);

    // Check if MRN already exists
    boolean existsByMrn(String mrn);

    // Check if contact number already exists
    boolean existsByContactNumber(String contactNumber);

    // Search patients by name (case-insensitive)
    List<Patient> findByFullNameContainingIgnoreCase(String fullName);

    // Search by name or MRN
    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "p.mrn LIKE CONCAT('%', :keyword, '%')")
    List<Patient> searchPatients(@Param("keyword") String keyword);
}
