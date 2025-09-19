package ru.virtusystems.domain.model;

import lombok.*;
import ru.virtusystems.domain.model.types.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Insured {

    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthDate;
    private String birthPlace;

    private String citizenship;

    private Gender gender;

    private String inn;
    private String snils;

    private String phone;
    private String email;

    private String docType;
    private String docSeries;
    private String docNumber;
    private String docIssuedBy;
    private LocalDate docIssuedDate;
    private String docDeptCode;

    private String regCountry;
    private String regRegion;
    private String regDistrict;
    private String regIndex;
    private String regCity;
    private String regStreet;
    private String regHouse;
    private String regBuilding;
    private String regApartment;
    private String regAddressLine;
}
