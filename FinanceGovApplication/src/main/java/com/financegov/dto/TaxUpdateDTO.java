package com.financegov.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TaxUpdateDTO {

    @NotNull(message = "Tax Record ID is required")
    private Long taxId;

    @DecimalMin(value = "0.00", message = "Amount cannot be negative")
    private BigDecimal amount;

    private Integer year;
    
    
}