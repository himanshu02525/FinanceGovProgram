package com.financegov.dto;
import com.financegov.enums.Type;

import jakarta.validation.constraints.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenBusinessRequestDTO {
   @NotBlank
   private String name;
   private Type type;
   private String address;
   @Pattern(regexp = "^[0-9]{10}$")
   private String contactInfo;
}