package com.hospital.pharmacy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false, length = 100)
    private String medicationName;

    @Column(length = 50)
    private String dosage;

    @Column(length = 50)
    private String frequency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DispenseStatus dispenseStatus;

    public Prescription() {
        this.dispenseStatus = DispenseStatus.PENDING;
    }

    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public DispenseStatus getDispenseStatus() { return dispenseStatus; }
    public void setDispenseStatus(DispenseStatus dispenseStatus) { this.dispenseStatus = dispenseStatus; }
}

