package com.hospital.pharmacy.dto;

public class InteractionResult {

    private String drugA;
    private String drugB;
    private String severity; // NONE, MODERATE, CRITICAL
    private String message;
    private boolean safe;

    public InteractionResult() {}

    public InteractionResult(String drugA, String drugB, String severity, String message, boolean safe) {
        this.drugA = drugA;
        this.drugB = drugB;
        this.severity = severity;
        this.message = message;
        this.safe = safe;
    }

    public String getDrugA() { return drugA; }
    public void setDrugA(String drugA) { this.drugA = drugA; }
    public String getDrugB() { return drugB; }
    public void setDrugB(String drugB) { this.drugB = drugB; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isSafe() { return safe; }
    public void setSafe(boolean safe) { this.safe = safe; }
}

