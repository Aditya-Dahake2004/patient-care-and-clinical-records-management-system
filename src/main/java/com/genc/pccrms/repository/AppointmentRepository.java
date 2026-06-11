package com.genc.pccrms.repository;

import com.genc.pccrms.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Get all appointments for a specific patient
    List<Appointment> findByPatient_PatientId(Integer patientId);

    // Get appointments by status
    List<Appointment> findByAppointmentStatus(Appointment.AppointmentStatus status);

    // Get appointments by doctor name
    List<Appointment> findByDoctorNameContainingIgnoreCase(String doctorName);
}