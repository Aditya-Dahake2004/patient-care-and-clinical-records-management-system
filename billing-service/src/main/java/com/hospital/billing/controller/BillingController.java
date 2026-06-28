package com.hospital.billing.controller;

import com.hospital.billing.dto.InvoiceRequest;
import com.hospital.billing.model.Invoice;
import com.hospital.billing.service.BillingService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/invoice")
    public ResponseEntity<?> generateInvoice(@RequestBody InvoiceRequest request) {
        try {
            Invoice invoice = billingService.generateInvoice(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable("id") Long invoiceId) {
        Optional<Invoice> invoice = billingService.getInvoiceById(invoiceId);
        if (invoice.isPresent()) {
            return ResponseEntity.ok(invoice.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Invoice not found with ID: " + invoiceId));
    }

    @PostMapping("/invoice/{id}/claim")
    public ResponseEntity<?> submitInsuranceClaim(@PathVariable("id") Long invoiceId) {
        try {
            Invoice invoice = billingService.submitInsuranceClaim(invoiceId);
            return ResponseEntity.ok(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/invoice/{id}/pay")
    public ResponseEntity<?> recordPayment(@PathVariable("id") Long invoiceId) {
        try {
            Invoice invoice = billingService.recordPayment(invoiceId);
            return ResponseEntity.ok(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Invoice>> getPatientInvoices(@PathVariable("patientId") Long patientId) {
        List<Invoice> invoices = billingService.getPatientInvoices(patientId);
        return ResponseEntity.ok(invoices);
    }
}

