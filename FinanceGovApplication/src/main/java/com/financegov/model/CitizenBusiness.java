package com.financegov.model;

import java.util.List;

import com.financegov.enums.Status;
import com.financegov.enums.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "citizen_business")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenBusiness {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long entityId;

	@NotBlank(message = "Name is required")
	@Size(max = 15, message = "Name must not exceed 15 characters")
	private String name;

	@NotNull(message = "Type is required")
	@Enumerated(EnumType.STRING)
	private Type type;

	private String address;

	@NotBlank(message = "Contact number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Only digits allowed")
	private String contactInfo;

	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(mappedBy = "citizenBusiness")
	private List<EntityDocument> documents;
	
	
}