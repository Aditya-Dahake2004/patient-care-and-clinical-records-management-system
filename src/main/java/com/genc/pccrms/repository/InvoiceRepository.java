package com.genc.pccrms.repository;

import com.genc.pccrms.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    List<Invoice> findByPatient_PatientId(Integer patientId);

    List<Invoice> findByClaimStatus(Invoice.ClaimStatus claimStatus);
}