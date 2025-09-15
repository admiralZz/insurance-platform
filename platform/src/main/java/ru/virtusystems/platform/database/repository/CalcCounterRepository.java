package ru.virtusystems.platform.database.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.virtusystems.database.model.CalcCounter;

import java.time.LocalDate;
import java.util.Optional;

public interface CalcCounterRepository extends JpaRepository<CalcCounter, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // ставим блокировку на строку
    @Query("select c from CalcCounter c where c.day = :day")
    Optional<CalcCounter> findByDayForUpdate(@Param("day") LocalDate day);
}


