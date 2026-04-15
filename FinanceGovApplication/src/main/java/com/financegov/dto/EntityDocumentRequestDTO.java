package com.financegov.dto;

import com.financegov.enums.DocType;

import jakarta.validation.constraints.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityDocumentRequestDTO {
   @NotNull
   private DocType docType;
   @Pattern(regexp = ".*\\.pdf$")
   private String fileURI;
   @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
   private String uploadedDate;
}