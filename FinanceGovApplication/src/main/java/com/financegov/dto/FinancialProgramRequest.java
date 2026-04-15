//package com.financegov.dto;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import com.financegov.model.SubsidyApplication;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.Future;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Positive;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//@Table(name = "financial_program")
//@Data                     // Generates getters, setters, toString, equals, hashCode
//@NoArgsConstructor        
//@AllArgsConstructor       
//public class FinancialProgramRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long programId;
//
//    @NotBlank(message = "Title cannot be blank")
//    private String title;
//
//    @NotBlank(message = "Description cannot be blank")
//    private String description;
//
//    @NotNull(message = "Start date is required")
//    private LocalDate startDate;
//
//    @NotNull(message = "End date is required")
//    @Future(message = "End date must be in the future")
//    private LocalDate endDate;
//
//    @NotNull(message = "Budget is required")
//    @Positive(message = "Budget must be a positive value")
//    private Double budget;
//
//    @NotBlank(message = "Status cannot be blank")
//    private String status;
//    
//    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SubsidyApplication> applications;
//}


package com.financegov.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProgramRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    // Optional: if not provided, service will default to LocalDate.now()
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be a positive value")
    private Double budget;

    // Optional: if not provided, service will default to ACTIVE
    private String status;
}

