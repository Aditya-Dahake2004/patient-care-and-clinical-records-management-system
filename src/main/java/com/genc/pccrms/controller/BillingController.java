package com.genc.pccrms.controller;

import com.genc.pccrms.model.Invoice;
import com.genc.pccrms.service.BillingService;
import com.genc.pccrms.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private PatientService patientService;

    // List all invoices
    @GetMapping
    public String listInvoices(Model model) {
        model.addAttribute("invoices", billingService.getAllInvoices());
        return "billing/list";
    }

    // Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("patients", patientService.getAllPatients());
        return "billing/create";
    }

    // Submit create form
    @PostMapping("/create")
    public String generateInvoice(@Valid @ModelAttribute("invoice") Invoice invoice,
                                  BindingResult result,
                                  @RequestParam(value = "patientId", required = false) String patientId,
                                  Model model) {
        Integer resolvedPatientId = resolvePatientId(patientId, result);
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            return "billing/create";
        }
        billingService.generateInvoice(resolvedPatientId, invoice);
        return "redirect:/billing";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Invoice invoice = billingService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        model.addAttribute("invoice", invoice);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("statuses", Invoice.ClaimStatus.values());
        return "billing/edit";
    }

    // Submit edit form
    @PostMapping("/edit/{id}")
    public String updateInvoice(@PathVariable Integer id,
                                @Valid @ModelAttribute("invoice") Invoice invoice,
                                BindingResult result,
                                @RequestParam(value = "patientId", required = false) String patientId,
                                Model model) {
        Integer resolvedPatientId = resolvePatientId(patientId, result);
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("statuses", Invoice.ClaimStatus.values());
            return "billing/edit";
        }
        billingService.updateInvoice(id, invoice, resolvedPatientId);
        return "redirect:/billing";
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

    // Approve claim
    @GetMapping("/approve/{id}")
    public String approveClaim(@PathVariable Integer id) {
        billingService.approveClaim(id);
        return "redirect:/billing";
    }

    // Reject claim
    @GetMapping("/reject/{id}")
    public String rejectClaim(@PathVariable Integer id) {
        billingService.rejectClaim(id);
        return "redirect:/billing";
    }

    // View invoice
    @GetMapping("/view/{id}")
    public String viewInvoice(@PathVariable Integer id, Model model) {
        Invoice invoice = billingService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        model.addAttribute("invoice", invoice);
        return "billing/view";
    }

    // Delete invoice
    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Integer id) {
        billingService.deleteInvoice(id);
        return "redirect:/billing";
    }
}