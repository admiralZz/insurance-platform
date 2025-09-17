package ru.virtusystems.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class InsuredRequiredParams {
    String firstName;
    String lastName;
    LocalDate birthDate;
    String passportSerial;
    String passportNumber;
}
