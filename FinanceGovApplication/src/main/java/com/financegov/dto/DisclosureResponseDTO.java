package com.financegov.dto;

import com.financegov.enums.DisclosureStatus;
import com.financegov.enums.DisclosureType;

import lombok.Data;

@Data
public class DisclosureResponseDTO {
    private Long disclosureId;
    private Long entityId;
    private DisclosureType type;
    private DisclosureStatus status;
}