package org.khtml.hexagonal.domain.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank String providerId,
        @NotBlank String username,
        @NotBlank String phoneNumber,
        @NotBlank String userType,
        @NotNull Boolean locationConsent,
        @NotNull Integer numberOfPersons
) {
}
