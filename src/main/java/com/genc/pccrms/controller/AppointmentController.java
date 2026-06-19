package com.genc.pccrms.controller;

import com.genc.pccrms.model.Appointment;
import com.genc.pccrms.service.AppointmentService;
import com.genc.pccrms.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    // List all appointments
    @GetMapping
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointment/list";
    }

    // Show book form
    @GetMapping("/book")
    public String showBookForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientService.getAllPatients());
        return "appointment/book";
    }

    // Submit book form
    @PostMapping("/book")
    public String bookAppointment(@Valid @ModelAttribute("appointment") Appointment appointment,
                                  BindingResult result,
                                  @RequestParam(value = "patientId", required = false) String patientId,
                                  Model model) {
        Integer resolvedPatientId = resolvePatientId(patientId, result);
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            return "appointment/book";
        }
        appointmentService.bookAppointment(resolvedPatientId, appointment);
        return "redirect:/appointments";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        model.addAttribute("appointment", appointment);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("statuses", Appointment.AppointmentStatus.values());
        return "appointment/edit";
    }

    // Submit edit form
    @PostMapping("/edit/{id}")
    public String updateAppointment(@PathVariable Integer id,
                                    @Valid @ModelAttribute("appointment") Appointment appointment,
                                    BindingResult result,
                                    @RequestParam(value = "patientId", required = false) String patientId,
                                    Model model) {
        Integer resolvedPatientId = resolvePatientId(patientId, result);
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("statuses", Appointment.AppointmentStatus.values());
            return "appointment/edit";
        }
        appointmentService.updateAppointment(id, appointment, resolvedPatientId);
        return "redirect:/appointments";
    }

    private Integer resolvePatientId(String patientId, BindingResult result) {
        if (!StringUtils.hasText(patientId)) {
            result.rejectValue("patient", "patient.required", "Patient is required");
            return null;
        }
        try {
            return Integer.valueOf(patientId);
        } catch (NumberFormatException ex) {
            result.rejectValue("patient", "patient.invalid", "Invalid patient selection");
            return null;
        }
    }

    // Delete appointment
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}