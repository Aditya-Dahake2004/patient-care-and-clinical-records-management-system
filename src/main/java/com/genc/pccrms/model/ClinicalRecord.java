package com.genc.pccrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "ClinicalRecord")
public class ClinicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @NotNull(message = "Encounter date is required")
    @PastOrPresent(message = "Encounter date cannot be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate encounterDate;

    @NotBlank(message = "Diagnosis code is required")
    @Pattern(regexp = "^[A-Z0-9]{2,20}$", message = "Diagnosis code must be alphanumeric (2-20 characters)")
    @Column(nullable = false, length = 20)
    private String diagnosisCode;

    @NotBlank(message = "Clinical notes are required")
    @Size(min = 5, max = 2000, message = "Clinical notes must be between 5 and 2000 characters")
    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @Size(max = 255, message = "Vitals summary must not exceed 255 characters")
    @Column(length = 255)
    private String vitalsSummary;

    public ClinicalRecord() {}

    public ClinicalRecord(Integer recordId, Patient patient, LocalDate encounterDate,
                          String diagnosisCode, String clinicalNotes, String vitalsSummary) {
        this.recordId = recordId;
        this.patient = patient;
        this.encounterDate = encounterDate;
        this.diagnosisCode = diagnosisCode;
        this.clinicalNotes = clinicalNotes;
        this.vitalsSummary = vitalsSummary;
    }

    public Integer getRecordId() { return recordId; }
    public Patient getPatient() { return patient; }
    public LocalDate getEncounterDate() { return encounterDate; }
    public String getDiagnosisCode() { return diagnosisCode; }
    public String getClinicalNotes() { return clinicalNotes; }
    public String getVitalsSummary() { return vitalsSummary; }

    public void setRecordId(Integer recordId) { this.recordId = recordId; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setEncounterDate(LocalDate encounterDate) { this.encounterDate = encounterDate; }
    public void setDiagnosisCode(String diagnosisCode) { this.diagnosisCode = diagnosisCode; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }
    public void setVitalsSummary(String vitalsSummary) { this.vitalsSummary = vitalsSummary; }
}