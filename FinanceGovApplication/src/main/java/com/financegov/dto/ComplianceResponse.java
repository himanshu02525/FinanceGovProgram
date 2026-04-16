package com.financegov.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.financegov.enums.ComplianceRecordResult;
import com.financegov.enums.ComplianceRecordType;

import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ComplianceResponse {

	private Long complianceId;
	private Long referenceId;
	private Long entityId;

	private ComplianceRecordType type;

	private ComplianceRecordResult result;

	private LocalDateTime createdAt;

	private LocalDateTime closedAt;

	private String notes;
	private TaxRequestDTO tax;
	private SubsidyResponse subsidyResponse;
	private FinancialProgramResponse financialProgramResponse;
}
