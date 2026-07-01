package com.genc.pharmacy_service.repository;

import com.genc.pharmacy_service.model.DrugInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, Long> {

    // Find interactions between two drugs
    @Query("SELECT d FROM DrugInteraction d WHERE " +
           "(LOWER(d.drug1Name) = LOWER(:drug1) AND LOWER(d.drug2Name) = LOWER(:drug2)) OR " +
           "(LOWER(d.drug1Name) = LOWER(:drug2) AND LOWER(d.drug2Name) = LOWER(:drug1))")
    List<DrugInteraction> findInteractionsBetween(@Param("drug1") String drug1, @Param("drug2") String drug2);

    // Find all interactions for a drug
    @Query("SELECT d FROM DrugInteraction d WHERE " +
           "LOWER(d.drug1Name) = LOWER(:drugName) OR LOWER(d.drug2Name) = LOWER(:drugName)")
    List<DrugInteraction> findAllInteractionsFor(@Param("drugName") String drugName);

    // Find severe interactions
    List<DrugInteraction> findBySeverity(String severity);
}

