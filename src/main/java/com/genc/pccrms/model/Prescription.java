package com.genc.pccrms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prescriptionId;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @Column(nullable = false, length = 100)
    private String medicationName;

    @Column(nullable = false, length = 50)
    private String dosage;

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