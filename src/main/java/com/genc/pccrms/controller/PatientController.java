package com.genc.pccrms.controller;

import com.genc.pccrms.model.Patient;
import com.genc.pccrms.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;


    @GetMapping
    public String listPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patient/list";
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/register";
    }

    @PostMapping("/register")
    public String registerPatient(@Valid @ModelAttribute("patient") Patient patient,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "patient/register";
        }
        patientService.registerPatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "patient/edit";
    }


    @PostMapping("/edit/{id}")
    public String updatePatient(@PathVariable Integer id,
                                @Valid @ModelAttribute("patient") Patient patient,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "patient/edit";
        }
        patientService.updatePatient(id, patient);
        return "redirect:/patients";
    }


    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }


    @GetMapping("/search")
    public String searchPatients(@RequestParam("keyword") String keyword, Model model) {
        List<Patient> patients = patientService.searchPatients(keyword);
        model.addAttribute("patients", patients);
        return "patient/list";
    }
}