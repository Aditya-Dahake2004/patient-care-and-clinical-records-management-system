package com.hospital.billing.service;

import com.hospital.billing.dto.InvoiceRequest;
import com.hospital.billing.model.ClaimStatus;
import com.hospital.billing.model.Invoice;
import com.hospital.billing.repository.InvoiceRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final Random random = new Random();

    // Insurance coverage percentage (80%)
    private static final BigDecimal INSURANCE_COVERAGE_RATE = new BigDecimal("0.80");

    public BillingService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Invoice generateInvoice(InvoiceRequest request) {
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (request.getTotalAmount() == null || request.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }

        Invoice invoice = new Invoice();
        invoice.setPatientId(request.getPatientId());
        invoice.setTotalAmount(request.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
        invoice.setClaimStatus(ClaimStatus.PENDING);

        if (request.isHasInsurance()) {
            // Calculate 80% insurance coverage, 20% patient payable
            BigDecimal insuranceCoverage = request.getTotalAmount()
                    .multiply(INSURANCE_COVERAGE_RATE)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal patientPayable = request.getTotalAmount()
                    .subtract(insuranceCoverage)
                    .setScale(2, RoundingMode.HALF_UP);

            invoice.setInsuranceCoverage(insuranceCoverage);
            invoice.setPatientPayable(patientPayable);
        } else {
            // No insurance - patient pays full amount
            invoice.setInsuranceCoverage(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            invoice.setPatientPayable(request.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
        }

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice submitInsuranceClaim(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        if (invoice.getInsuranceCoverage() == null || 
            invoice.getInsuranceCoverage().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("This invoice has no insurance coverage to claim");
        }

        if (invoice.getClaimStatus() != ClaimStatus.PENDING) {
            throw new IllegalStateException("Claim has already been processed. Current status: " + invoice.getClaimStatus());
        }

        // Mock insurance API call - for predictable testing, we'll approve most claims
        // In real scenario, this would call an external insurance payer API
        boolean isApproved = random.nextInt(10) < 8; // 80% approval rate
        
        if (isApproved) {
            invoice.setClaimStatus(ClaimStatus.APPROVED);
        } else {
            // Claim rejected - patient now owes full amount
            invoice.setClaimStatus(ClaimStatus.REJECTED);
            invoice.setPatientPayable(invoice.getTotalAmount());
            invoice.setInsuranceCoverage(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice recordPayment(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        if (invoice.isPaid()) {
            throw new IllegalStateException("This invoice has already been paid");
        }

        invoice.setPatientPayable(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        invoice.setPaid(true);
        invoice.setPaidAt(LocalDateTime.now());

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getPatientInvoices(Long patientId) {
        return invoiceRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public Optional<Invoice> getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }
}

