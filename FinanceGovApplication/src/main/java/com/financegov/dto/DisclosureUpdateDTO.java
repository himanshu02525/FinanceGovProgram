package com.financegov.dto;

import com.financegov.enums.DisclosureType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisclosureUpdateDTO {

    @NotNull(message = "Disclosure ID is required for updates")
    private Long disclosureId;

    private DisclosureType type; // Optional: only if changing from INCOME to EXPENSE

   

   
}
