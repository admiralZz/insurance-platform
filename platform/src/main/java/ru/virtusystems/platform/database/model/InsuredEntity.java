package ru.virtusystems.platform.database.model;

import jakarta.persistence.*;
import lombok.*;
import ru.virtusystems.domain.model.types.Gender;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insured")
public class InsuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private LocalDate birthDate;
    private String birthPlace;

    @Column(nullable = false)
    private String citizenship;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
