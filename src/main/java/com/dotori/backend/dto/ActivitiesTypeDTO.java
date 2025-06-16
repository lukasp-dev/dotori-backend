package com.dotori.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Activities type information")
public class ActivitiesTypeDTO {
  @NotBlank
  @Schema(description = "Activities type id", example = "1")
  private Long id;

  @NotBlank
  @Schema(description = "Activities type name", example = "Running")
  private String name;
}
