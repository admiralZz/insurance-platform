package ru.virtusystems.platform.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.virtusystems.platform.database.model.InsuredEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface InsuredEntityRepository extends JpaRepository<InsuredEntity, Long> {
    Optional<InsuredEntity> findInsuredByFirstNameAndLastNameAndBirthDateAndDocSeriesAndDocNumber(
            String firstName, String lastName, LocalDate birthDate, String docSeries, String docNumber
    );
}


