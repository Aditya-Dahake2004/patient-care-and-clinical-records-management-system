package com.genc.pccrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Insurance coverage is required")
    @DecimalMin(value = "0.00", message = "Insurance coverage cannot be negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal insuranceCoverage;

    @DecimalMin(value = "0.00", message = "Patient payable amount cannot be negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal patientPayable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus claimStatus;

    public enum ClaimStatus {
        PENDING, APPROVED, REJECTED
    }

    public Invoice() {}



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