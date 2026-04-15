//package com.financegov.model;
//
//import java.time.LocalDate;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.*;
//import com.financegov.enums.SubsidyStatus;
//
//@Entity
//@Table(name = "subsidy")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Subsidy {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long subsidyId;
//
//    @NotNull(message = "Entity ID is required")
//    @Positive(message = "Entity ID must be positive")
//    private Long entityId;
//
//    @ManyToOne
//    @JoinColumn(name = "program_id", nullable = false)
//    private FinancialProgram program;
//
//    @NotNull(message = "Amount is required")
//    @Positive(message = "Amount must be positive")
//    private Double amount;
//
//    @NotNull(message = "Date is required")
//    @PastOrPresent(message = "Date cannot be in the future")
//    private LocalDate date;
//
//    @Enumerated(EnumType.STRING)
//    @NotNull(message = "Subsidy status is required")
//    private SubsidyStatus status;
//}


package com.financegov.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.financegov.enums.SubsidyStatus;

@Entity
@Table(name = "subsidy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subsidy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subsidyId;

    @ManyToOne
    @JoinColumn(name = "entity_id", nullable = false)   // FK to citizen_business
    private CitizenBusiness citizenBusiness;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private FinancialProgram program;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private SubsidyStatus status;

}

