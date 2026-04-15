//package com.financegov.dto;
//
//import java.time.LocalDate;
//
//import com.financegov.enums.ApplicationStatus;
//
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class SubsidyApplicationResponse {
//
//    private Long applicationId;
//    private Long entityId;
//    private LocalDate submittedDate;
//    
//    private Long programId;   // Expose only programId, not the full FinancialProgram object
//    
//    private ApplicationStatus status;
//}


package com.financegov.dto;

import java.time.LocalDate;
import com.financegov.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubsidyApplicationResponse {
    private Long applicationId;      // Auto-generated ID
    private Long entityId;           // Citizen/Business ID
    private LocalDate submittedDate; // Date application was created
    private Long programId;          // Linked program ID
    private ApplicationStatus status; // GRANTED / CANCELLED
}
