package com.genc.pccrms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ClinicalRecord")
public class ClinicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDate encounterDate;

    @Column(nullable = false, length = 20)
    private String diagnosisCode;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

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