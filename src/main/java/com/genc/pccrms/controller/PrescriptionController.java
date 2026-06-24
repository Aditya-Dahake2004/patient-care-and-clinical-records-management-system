package com.genc.pccrms.controller;

import com.genc.pccrms.model.Prescription;
import com.genc.pccrms.service.PatientService;
import com.genc.pccrms.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PatientService patientService;

    // List all prescriptions
    @GetMapping
    public String listPrescriptions(Model model) {
        model.addAttribute("prescriptions", prescriptionService.getAllPrescriptions());
        return "prescription/list";
    }

    // Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("prescription", new Prescription());
        model.addAttribute("patients", patientService.getAllPatients());
        return "prescription/create";
    }

    // Submit create form
    @PostMapping("/create")
    public String createPrescription(@Valid @ModelAttribute("prescription") Prescription prescription,
                                     BindingResult result,
                                     Model model) {
        Integer resolvedPatientId = resolvePatientId(prescription, result);
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            return "prescription/create";
        }
        prescriptionService.createPrescription(resolvedPatientId, prescription);
        return "redirect:/prescriptions";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Prescription prescription = prescriptionService.getPrescriptionById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        model.addAttribute("prescription", prescription);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("statuses", Prescription.DispenseStatus.values());
        return "prescription/edit";
    }

    // Submit edit form
    @PostMapping("/edit/{id}")
    public String updatePrescription(@PathVariable Integer id,
                                     @Valid @ModelAttribute("prescription") Prescription prescription,
                                     BindingResult result,
                                     Model model) {
        Integer resolvedPatientId = resolvePatientId(prescription, result);
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("statuses", Prescription.DispenseStatus.values());
            return "prescription/edit";
        }
        prescriptionService.updatePrescription(id, prescription, resolvedPatientId);
        return "redirect:/prescriptions";
    }
//custom validation method
    private Integer resolvePatientId(Prescription prescription, BindingResult result) {
        if (prescription.getPatient() == null || prescription.getPatient().getPatientId() == null) {
            result.rejectValue("patient.patientId", "patient.required", "Patient is required");
            return null;
        }
        return prescription.getPatient().getPatientId();
    }

    // Dispense medication
    @GetMapping("/dispense/{id}")
    public String dispenseMedication(@PathVariable Integer id) {
        prescriptionService.dispenseMedication(id);
        return "redirect:/prescriptions";
    }

    // Delete prescription
    @GetMapping("/delete/{id}")
    public String deletePrescription(@PathVariable Integer id) {
        prescriptionService.deletePrescription(id);
        return "redirect:/prescriptions";
    }
}