package com.genc.pccrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 2, max = 100, message = "Doctor name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String doctorName;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;

    public enum AppointmentStatus {
        BOOKED, COMPLETED, CANCELLED, NO_SHOW
    }

    // ===== Constructors =====
    public Appointment() {}

    public Appointment(Integer appointmentId, Patient patient, String doctorName,
                       LocalDate appointmentDate, LocalTime appointmentTime,
                       AppointmentStatus appointmentStatus) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
    }

    // ===== Getters =====
    public Integer getAppointmentId() { return appointmentId; }
    public Patient getPatient() { return patient; }
    public String getDoctorName() { return doctorName; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public AppointmentStatus getAppointmentStatus() { return appointmentStatus; }

    // ===== Setters =====
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setAppointmentStatus(AppointmentStatus appointmentStatus) { this.appointmentStatus = appointmentStatus; }
}