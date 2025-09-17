package ru.virtusystems.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.types.Gender;

import java.time.LocalDate;

@Value
@Builder
public class CreateInsuredDto {
    @NotBlank
    String lastName;
    @NotBlank
    String firstName;
    String middleName;
    @NotNull
    LocalDate birthDate;
    String birthPlace;
    @NotBlank
    String citizenship;
    @NotNull
    Gender gender;
    String inn;
    String snils;
    String phone;
    String email;
    String docType;
    String docSeries;
    String docNumber;
    String docIssuedBy;
    LocalDate docIssuedDate;
    String docDeptCode;
    String regCountry;
    String regRegion;
    String regDistrict;
    String regIndex;
    String regCity;
    String regStreet;
    String regHouse;
    String regBuilding;
    String regApartment;
    String regAddressLine;
}
