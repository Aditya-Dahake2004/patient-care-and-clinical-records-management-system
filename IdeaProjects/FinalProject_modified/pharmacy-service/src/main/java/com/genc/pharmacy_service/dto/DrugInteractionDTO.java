package com.genc.pharmacy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugInteractionDTO {

    private Long interactionId;
    private String drug1Name;
    private String drug2Name;
    private String severity;
    private String description;
    private String recommendation;
}

