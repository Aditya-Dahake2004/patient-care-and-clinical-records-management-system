package com.genc.pharmacy_service.config;

import com.genc.pharmacy_service.model.DrugInteraction;
import com.genc.pharmacy_service.model.Medication;
import com.genc.pharmacy_service.repository.DrugInteractionRepository;
import com.genc.pharmacy_service.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DrugInteractionRepository drugInteractionRepository;
    private final MedicationRepository medicationRepository;

    @Override
    public void run(String... args) {
        initializeDrugInteractions();
        initializeMedications();
    }

    private void initializeDrugInteractions() {
        if (drugInteractionRepository.count() == 0) {
            log.info("Initializing drug interactions data...");

            // Sample drug interactions
            drugInteractionRepository.save(DrugInteraction.builder()
                    .drug1Name("Aspirin")
                    .drug2Name("Warfarin")
                    .severity("SEVERE")
                    .description("Increased risk of bleeding when taken together")
                    .recommendation("Avoid concurrent use or monitor closely")
                    .build());

            drugInteractionRepository.save(DrugInteraction.builder()
                    .drug1Name("Ibuprofen")
                    .drug2Name("Aspirin")
                    .severity("MODERATE")
                    .description("May reduce the cardioprotective effects of aspirin")
                    .recommendation("Take ibuprofen at least 30 minutes after aspirin")
                    .build());

            drugInteractionRepository.save(DrugInteraction.builder()
                    .drug1Name("Lisinopril")
                    .drug2Name("Potassium")
                    .severity("MODERATE")
                    .description("May cause high potassium levels")
                    .recommendation("Monitor potassium levels regularly")
                    .build());

            drugInteractionRepository.save(DrugInteraction.builder()
                    .drug1Name("Metformin")
                    .drug2Name("Alcohol")
                    .severity("SEVERE")
                    .description("Increased risk of lactic acidosis")
                    .recommendation("Avoid alcohol consumption while on metformin")
                    .build());

            drugInteractionRepository.save(DrugInteraction.builder()
                    .drug1Name("Simvastatin")
                    .drug2Name("Grapefruit")
                    .severity("MODERATE")
                    .description("Grapefruit increases statin levels in blood")
                    .recommendation("Avoid grapefruit or grapefruit juice")
                    .build());

            log.info("Drug interactions data initialized");
        }
    }

    private void initializeMedications() {
        if (medicationRepository.count() == 0) {
            log.info("Initializing medications inventory...");

            medicationRepository.save(Medication.builder()
                    .name("Paracetamol 500mg")
                    .genericName("Acetaminophen")
                    .manufacturer("Generic Pharma")
                    .category("Painkiller")
                    .dosageForm("Tablet")
                    .strength("500mg")
                    .stockQuantity(1000)
                    .reorderLevel(200)
                    .unitPrice(new BigDecimal("0.50"))
                    .expiryDate(LocalDate.now().plusYears(2))
                    .contraindications("Liver disease")
                    .active(true)
                    .build());

            medicationRepository.save(Medication.builder()
                    .name("Amoxicillin 500mg")
                    .genericName("Amoxicillin")
                    .manufacturer("Pharma Corp")
                    .category("Antibiotic")
                    .dosageForm("Capsule")
                    .strength("500mg")
                    .stockQuantity(500)
                    .reorderLevel(100)
                    .unitPrice(new BigDecimal("1.20"))
                    .expiryDate(LocalDate.now().plusYears(1))
                    .contraindications("Penicillin allergy")
                    .active(true)
                    .build());

            medicationRepository.save(Medication.builder()
                    .name("Ibuprofen 400mg")
                    .genericName("Ibuprofen")
                    .manufacturer("Pain Relief Inc")
                    .category("NSAID")
                    .dosageForm("Tablet")
                    .strength("400mg")
                    .stockQuantity(800)
                    .reorderLevel(150)
                    .unitPrice(new BigDecimal("0.75"))
                    .expiryDate(LocalDate.now().plusYears(2))
                    .contraindications("Stomach ulcers, aspirin allergy")
                    .active(true)
                    .build());

            medicationRepository.save(Medication.builder()
                    .name("Omeprazole 20mg")
                    .genericName("Omeprazole")
                    .manufacturer("GastroMed")
                    .category("Antacid")
                    .dosageForm("Capsule")
                    .strength("20mg")
                    .stockQuantity(600)
                    .reorderLevel(100)
                    .unitPrice(new BigDecimal("0.90"))
                    .expiryDate(LocalDate.now().plusMonths(18))
                    .contraindications("None significant")
                    .active(true)
                    .build());

            medicationRepository.save(Medication.builder()
                    .name("Metformin 500mg")
                    .genericName("Metformin HCL")
                    .manufacturer("DiabetesCare")
                    .category("Antidiabetic")
                    .dosageForm("Tablet")
                    .strength("500mg")
                    .stockQuantity(700)
                    .reorderLevel(150)
                    .unitPrice(new BigDecimal("0.60"))
                    .expiryDate(LocalDate.now().plusYears(2))
                    .contraindications("Kidney disease, alcohol")
                    .active(true)
                    .build());

            log.info("Medications inventory initialized");
        }
    }
}

