package com.financegov.dto;
import com.financegov.enums.*;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityDocumentResponseDTO {
   private Long documentId;
   private Long entityId;
   private DocType docType;
   private String fileURI;
   private String uploadedDate;
   private VerificationStatus verificationStatus;
}