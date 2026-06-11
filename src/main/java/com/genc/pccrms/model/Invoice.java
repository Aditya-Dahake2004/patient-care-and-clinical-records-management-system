package com.genc.pccrms.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal insuranceCoverage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal patientPayable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus claimStatus;

    public enum ClaimStatus {
        PENDING, APPROVED, REJECTED
    }

    public Invoice() {}

    public Invoice(Integer invoiceId, Patient patient, BigDecimal totalAmount,
                   BigDecimal insuranceCoverage, BigDecimal patientPayable,
                   ClaimStatus claimStatus) {
        this.invoiceId = invoiceId;
        this.patient = patient;
        this.totalAmount = totalAmount;
        this.insuranceCoverage = insuranceCoverage;
        this.patientPayable = patientPayable;
        this.claimStatus = claimStatus;
    }

    public Integer getInvoiceId() { return invoiceId; }
    public Patient getPatient() { return patient; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getInsuranceCoverage() { return insuranceCoverage; }
    public BigDecimal getPatientPayable() { return patientPayable; }
    public ClaimStatus getClaimStatus() { return claimStatus; }


    public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setInsuranceCoverage(BigDecimal insuranceCoverage) { this.insuranceCoverage = insuranceCoverage; }
    public void setPatientPayable(BigDecimal patientPayable) { this.patientPayable = patientPayable; }
    public void setClaimStatus(ClaimStatus claimStatus) { this.claimStatus = claimStatus; }
}