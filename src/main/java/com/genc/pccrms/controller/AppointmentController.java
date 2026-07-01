package com.genc.pccrms.controller;

import com.genc.pccrms.model.Appointment;
import com.genc.pccrms.service.AppointmentService;
import com.genc.pccrms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointment/list";
    }

    @GetMapping("/book")
    public String showBookForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientService.getAllPatients());
        return "appointment/book";
    }


    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment,
                                  @RequestParam("patientId") Integer patientId) {
        appointmentService.bookAppointment(patientId, appointment);
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
                                    @ModelAttribute("appointment") Appointment appointment,
                                    @RequestParam("patientId") Integer patientId) {
        appointmentService.updateAppointment(id, appointment, patientId);
        return "redirect:/appointments";
    }

    // Delete appointment
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}