package com.financegov.dto;

import com.financegov.enums.DisclosureType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisclosureCreateRequestDTO {
    @NotNull(message = "Entity ID is required")
    @Min(value = 1, message = "Entity ID must be at least 1")
    private Long entityId;

    @NotNull(message = "Type is required (INCOME or EXPENSE)")
    private DisclosureType type;
}