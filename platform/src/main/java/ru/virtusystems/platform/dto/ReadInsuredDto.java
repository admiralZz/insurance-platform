package ru.virtusystems.platform.dto;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.types.Gender;

import java.time.LocalDate;

@Value
@Builder
public class ReadInsuredDto {
    Long id;
    String lastName;
    String firstName;
    String middleName;
    LocalDate birthDate;
    String birthPlace;
    String citizenship;
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
