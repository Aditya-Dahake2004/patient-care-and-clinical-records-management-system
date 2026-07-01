package com.genc.pharmacy_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drug_interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interactionId;

    @Column(nullable = false, length = 100)
    private String drug1Name;

    @Column(nullable = false, length = 100)
    private String drug2Name;

    @Column(nullable = false, length = 20)
    private String severity; // MILD, MODERATE, SEVERE

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String recommendation;
}

