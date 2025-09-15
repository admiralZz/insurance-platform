package ru.virtusystems.platform.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.virtusystems.database.model.Insured;

import java.time.LocalDate;
import java.util.Optional;

public interface InsuredRepository extends JpaRepository<Insured, Long> {
    Optional<Insured> findInsuredByFirstNameAndLastNameAndBirthDateAndDocSeriesAndDocNumber(
            String firstName, String lastName, LocalDate birthDate, String docSeries, String docNumber
    );
}


