package com.genc.pccrms.service;

import com.genc.pccrms.model.Invoice;
import com.genc.pccrms.model.Patient;
import com.genc.pccrms.repository.InvoiceRepository;
import com.genc.pccrms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PatientRepository patientRepository;

    public Invoice generateInvoice(Integer patientId, Invoice invoice) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        invoice.setPatient(patient);

        BigDecimal payable = invoice.getTotalAmount()
                .subtract(invoice.getInsuranceCoverage());
        invoice.setPatientPayable(payable);

        invoice.setClaimStatus(Invoice.ClaimStatus.PENDING);

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Integer id) {
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getInvoicesByPatient(Integer patientId) {
        return invoiceRepository.findByPatient_PatientId(patientId);
    }

    public Invoice updateInvoice(Integer id, Invoice updated, Integer patientId) {
        Invoice existing = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setPatient(patient);
        existing.setTotalAmount(updated.getTotalAmount());
        existing.setInsuranceCoverage(updated.getInsuranceCoverage());

        BigDecimal payable = updated.getTotalAmount()
                .subtract(updated.getInsuranceCoverage());
        existing.setPatientPayable(payable);
        existing.setClaimStatus(updated.getClaimStatus());

        return invoiceRepository.save(existing);
    }

    public Invoice approveClaim(Integer id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setClaimStatus(Invoice.ClaimStatus.APPROVED);
        return invoiceRepository.save(invoice);
    }

    public Invoice rejectClaim(Integer id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setClaimStatus(Invoice.ClaimStatus.REJECTED);
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Integer id) {
        invoiceRepository.deleteById(id);
    }
}