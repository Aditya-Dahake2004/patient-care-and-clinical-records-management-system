package com.genc.pccrms.service;

import com.genc.pccrms.model.Appointment;
import com.genc.pccrms.model.Patient;
import com.genc.pccrms.repository.AppointmentRepository;
import com.genc.pccrms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Book new appointment
    public Appointment bookAppointment(Integer patientId, Appointment appointment) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        appointment.setPatient(patient);
        appointment.setAppointmentStatus(Appointment.AppointmentStatus.BOOKED);
        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Optional<Appointment> getAppointmentById(Integer id) {
        return appointmentRepository.findById(id);
    }

    // Get appointments by patient
    public List<Appointment> getAppointmentsByPatient(Integer patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    // Update appointment
    public Appointment updateAppointment(Integer id, Appointment updated, Integer patientId) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setPatient(patient);
        existing.setDoctorName(updated.getDoctorName());
        existing.setAppointmentDate(updated.getAppointmentDate());
        existing.setAppointmentTime(updated.getAppointmentTime());
        existing.setAppointmentStatus(updated.getAppointmentStatus());
        return appointmentRepository.save(existing);
    }

    // Delete appointment
    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteById(id);
    }
}