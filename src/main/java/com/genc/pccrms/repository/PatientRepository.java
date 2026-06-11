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

    Optional<Patient> findByMrn(String mrn);

    boolean existsByMrn(String mrn);


    boolean existsByContactNumber(String contactNumber);

    List<Patient> findByFullNameContainingIgnoreCase(String fullName);

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "p.mrn LIKE CONCAT('%', :keyword, '%')")
    List<Patient> searchPatients(@Param("keyword") String keyword);
}
