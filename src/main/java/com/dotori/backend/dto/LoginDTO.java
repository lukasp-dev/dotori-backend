package com.dotori.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User login information")
public class LoginDTO {
    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "password123")
    private String password;
}
