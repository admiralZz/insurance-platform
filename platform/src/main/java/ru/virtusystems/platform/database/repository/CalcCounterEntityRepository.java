package ru.virtusystems.platform.database.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.virtusystems.platform.database.model.CalcCounterEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface CalcCounterEntityRepository extends JpaRepository<CalcCounterEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // ставим блокировку на строку
    @Query("select c from CalcCounterEntity c where c.day = :day")
    Optional<CalcCounterEntity> findByDayForUpdate(@Param("day") LocalDate day);
}


