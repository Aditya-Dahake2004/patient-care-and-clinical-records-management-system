package com.genc.pccrms.controller;

import com.genc.pccrms.model.ClinicalRecord;
import com.genc.pccrms.service.ClinicalRecordService;
import com.genc.pccrms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ehr")
public class EhrController {

    @Autowired
    private ClinicalRecordService clinicalRecordService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public String listRecords(Model model) {
        model.addAttribute("records", clinicalRecordService.getAllRecords());
        return "ehr/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("record", new ClinicalRecord());
        model.addAttribute("patients", patientService.getAllPatients());
        return "ehr/create";
    }

    @PostMapping("/create")
    public String createRecord(@ModelAttribute("record") ClinicalRecord record,
                               @RequestParam("patientId") Integer patientId) {
        clinicalRecordService.createRecord(patientId, record);
        return "redirect:/ehr";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ClinicalRecord record = clinicalRecordService.getRecordById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        model.addAttribute("record", record);
        model.addAttribute("patients", patientService.getAllPatients());
        return "ehr/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRecord(@PathVariable Integer id,
                               @ModelAttribute("record") ClinicalRecord record,
                               @RequestParam("patientId") Integer patientId) {
        clinicalRecordService.updateRecord(id, record, patientId);
        return "redirect:/ehr";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Integer id) {
        clinicalRecordService.deleteRecord(id);
        return "redirect:/ehr";
    }
    @GetMapping("/view/{id}")
    public String viewRecord(@PathVariable Integer id, Model model) {
        ClinicalRecord record = clinicalRecordService.getRecordById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        model.addAttribute("record", record);
        return "ehr/view";
    }
}