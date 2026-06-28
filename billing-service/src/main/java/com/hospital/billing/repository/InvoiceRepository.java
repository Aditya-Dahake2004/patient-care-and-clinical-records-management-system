package com.hospital.billing.repository;

import com.hospital.billing.model.Invoice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}

