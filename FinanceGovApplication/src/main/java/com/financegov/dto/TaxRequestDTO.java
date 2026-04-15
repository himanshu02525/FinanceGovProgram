package com.financegov.dto;



import jakarta.validation.constraints.*;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class TaxRequestDTO {
	@NotNull(message = "Entity ID is required")
	private Long entityId;

	@NotNull(message = "Fiscal year is required")
    @Min(value = 2000, message = "Year must be at least 2000")
	private Integer year;

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.00", message = "Amount cannot be negative")
	private BigDecimal amount;

	
}