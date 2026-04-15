package com.financegov.model;

import java.math.BigDecimal;

import com.financegov.enums.TaxStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tax_records")
@Data
public class TaxRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taxId;

	// PIN-TO-PIN JOIN IMPLEMENTATION
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id", nullable = false)
	private CitizenBusiness citizenBusiness;

	@NotNull(message = "Fiscal year is required")
    @Min(value = 2000, message = "System does not support records before year 2000")
    @Column(nullable = false)
	private Integer year;

	@Column(precision = 19, scale = 2, nullable = false)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	private TaxStatus status;

	// Expected: "PAID", "PENDING", "OVERDUE"

	
}
