package com.financegov.dto;
import com.financegov.enums.*;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenBusinessResponseDTO {
   private Long entityId;
   private String name;
   private Type type;
   private String address;
   private String contactInfo;
   private Status status;
}