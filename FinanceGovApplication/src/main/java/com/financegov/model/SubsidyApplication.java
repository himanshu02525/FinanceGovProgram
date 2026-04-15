//package com.financegov.model;
//
//import java.time.LocalDate;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.*;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.financegov.enums.ApplicationStatus;
//
//@Entity
//@Table(name = "subsidy_application")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class SubsidyApplication {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long applicationId;
//
//    @NotNull(message = "Entity ID is required")
//    @Positive(message = "Entity ID must be positive")
//    private Long entityId;
//
//    @NotNull(message = "Submitted date is required")
//    @PastOrPresent(message = "Submitted date cannot be in the future")
//    private LocalDate submittedDate;
//
//    @ManyToOne
//    @JoinColumn(name = "program_id", nullable = false)
//    @JsonIgnore
//    private FinancialProgram program;
//
//    @Enumerated(EnumType.STRING)
//    @NotNull(message = "Application status is required")
//    private ApplicationStatus status;
//}


package com.financegov.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financegov.enums.ApplicationStatus;

@Entity
@Table(name = "subsidy_application")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsidyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "entityId", nullable = false)   // FK to citizen_business
    private CitizenBusiness citizenBusiness;

    @NotNull(message = "Submitted date is required")
    @PastOrPresent(message = "Submitted date cannot be in the future")
    private LocalDate submittedDate;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    @JsonIgnore
    private FinancialProgram program;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Application status is required")
    private ApplicationStatus status;
}
