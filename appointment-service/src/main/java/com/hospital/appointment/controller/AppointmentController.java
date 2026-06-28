package com.hospital.appointment.controller;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.StatusUpdateRequest;
import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        try {
            Appointment appointment = appointmentService.bookAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long appointmentId,
                                          @RequestBody StatusUpdateRequest request) {
        try {
            if (request.getStatus() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Status is required"));
            }
            Appointment appointment = appointmentService.updateAppointmentStatus(appointmentId, request.getStatus());
            return ResponseEntity.ok(appointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable("id") Long appointmentId) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Appointment not found with ID: " + appointmentId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable("doctorId") Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable("patientId") Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/slots")
    public ResponseEntity<List<String>> getAvailableSlots(
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        if (doctorId != null && date != null) {
            List<String> slots = appointmentService.getAvailableSlots(doctorId, date);
            return ResponseEntity.ok(slots);
        }
        // Return mock hardcoded slots if no params provided
        List<String> mockSlots = appointmentService.getMockAvailableSlots();
        return ResponseEntity.ok(mockSlots);
    }
}

