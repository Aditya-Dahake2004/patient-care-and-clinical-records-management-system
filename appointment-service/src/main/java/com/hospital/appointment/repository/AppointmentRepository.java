package com.hospital.appointment.repository;

import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdOrderByAppointmentDateDescAppointmentTimeDesc(Long doctorId);

    List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(Long patientId);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNot(
            Long doctorId, LocalDate appointmentDate, LocalTime appointmentTime, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndAppointmentDateAndAppointmentStatusNot(
            Long doctorId, LocalDate appointmentDate, AppointmentStatus status);
}

