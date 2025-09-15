package ru.virtusystems.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class InsuredRequiredParams {
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotNull
    LocalDate birthDate;
    @NotBlank
    String passportSerial;
    @NotBlank
    String passportNumber;
}
