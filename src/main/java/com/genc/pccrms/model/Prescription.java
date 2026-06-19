package com.genc.pccrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prescriptionId;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @NotBlank(message = "Medication name is required")
    @Size(min = 2, max = 100, message = "Medication name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String medicationName;

    @NotBlank(message = "Dosage is required")
    @Size(min = 1, max = 50, message = "Dosage must be between 1 and 50 characters")
    @Column(nullable = false, length = 50)
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(min = 1, max = 50, message = "Frequency must be between 1 and 50 characters")
    @Column(nullable = false, length = 50)
    private String frequency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DispenseStatus dispenseStatus;

    public enum DispenseStatus {
        PENDING, DISPENSED, CANCELLED
    }

    // ===== Constructors =====
    public Prescription() {}

    public Prescription(Integer prescriptionId, Patient patient, String medicationName,
                        String dosage, String frequency, DispenseStatus dispenseStatus) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.dispenseStatus = dispenseStatus;
    }

    // ===== Getters =====
    public Integer getPrescriptionId() { return prescriptionId; }
    public Patient getPatient() { return patient; }
    public String getMedicationName() { return medicationName; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public DispenseStatus getDispenseStatus() { return dispenseStatus; }

    // ===== Setters =====
    public void setPrescriptionId(Integer prescriptionId) { this.prescriptionId = prescriptionId; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setDispenseStatus(DispenseStatus dispenseStatus) { this.dispenseStatus = dispenseStatus; }
}