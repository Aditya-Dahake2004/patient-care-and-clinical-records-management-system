package com.genc.pccrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;

    @Column(unique = true, length = 20)
    private String mrn;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Column(nullable = false, length = 10)
    private String gender;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    @Column(nullable = false, length = 20)
    private String contactNumber;

    public Patient() {}

    public Patient(Integer patientId, String mrn, String fullName,
                   LocalDate dateOfBirth, String gender, String contactNumber) {
        this.patientId = patientId;
        this.mrn = mrn;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactNumber = contactNumber;
    }

    public Integer getPatientId() { return patientId; }
    public String getMrn() { return mrn; }
    public String getFullName() { return fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getContactNumber() { return contactNumber; }


    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public void setMrn(String mrn) { this.mrn = mrn; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setGender(String gender) { this.gender = gender; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}