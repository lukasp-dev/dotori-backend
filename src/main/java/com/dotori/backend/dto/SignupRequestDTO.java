package com.dotori.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "User signup information")
public class SignupRequestDTO {
    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @NotBlank
    @Length(min = 6, max = 150)
    @Schema(description = "User's password", example = "password123")
    private String password;

    @NotBlank
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank
    @Schema(description = "User's role", example = "USER")
    private String role;
} 