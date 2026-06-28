package com.hospital.appointment.service;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentStatus;
import com.hospital.appointment.repository.AppointmentRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    // Hardcoded available time slots
    private static final List<LocalTime> ALL_SLOTS = Arrays.asList(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0)
    );

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public Appointment bookAppointment(AppointmentRequest request) {
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (request.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID is required");
        }
        if (request.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        if (request.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time is required");
        }

        // Check for conflicting appointment (same doctor, date, time, and not cancelled)
        boolean hasConflict = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNot(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime(),
                        AppointmentStatus.CANCELLED
                );

        if (hasConflict) {
            throw new IllegalStateException("This time slot is already booked for the selected doctor");
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setDoctorId(request.getDoctorId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentStatus(AppointmentStatus.BOOKED);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        appointment.setAppointmentStatus(newStatus);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment rescheduleAppointment(Long appointmentId, LocalDate newDate, LocalTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        // Check for conflict at new slot
        boolean hasConflict = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNot(
                        appointment.getDoctorId(),
                        newDate,
                        newTime,
                        AppointmentStatus.CANCELLED
                );

        if (hasConflict) {
            throw new IllegalStateException("This time slot is already booked for the selected doctor");
        }

        appointment.setAppointmentDate(newDate);
        appointment.setAppointmentTime(newTime);
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDescAppointmentTimeDesc(doctorId);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patientId);
    }

    public List<String> getAvailableSlots(Long doctorId, LocalDate date) {
        // Get booked/completed/no-show appointments for this doctor on this date
        List<Appointment> bookedAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndAppointmentStatusNot(
                        doctorId, date, AppointmentStatus.CANCELLED);

        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toList());

        // Filter out booked times from all slots
        List<String> availableSlots = new ArrayList<>();
        for (LocalTime slot : ALL_SLOTS) {
            if (!bookedTimes.contains(slot)) {
                availableSlots.add(slot.toString());
            }
        }

        return availableSlots;
    }

    public List<String> getMockAvailableSlots() {
        return Arrays.asList("09:00", "10:00", "11:00", "14:00", "15:00", "16:00");
    }
}

