//package com.financegov.dto;
//
//import java.time.LocalDate;
//import com.financegov.enums.ApplicationStatus;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class SubsidyApplicationRequest {
//    private Long entityId;
//    private Long programId;
//    private LocalDate submittedDate;
//    private ApplicationStatus status; // optional, defaults to PENDING
//}


package com.financegov.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsidyApplicationRequest {

    @NotNull(message = "Entity ID is required")
    private Long entityId;   // Citizen/Business ID

    @NotNull(message = "Program ID is required")
    private Long programId;  // Linked program ID

    // Optional: defaults to LocalDate.now() when created automatically
    private LocalDate submittedDate;
}
