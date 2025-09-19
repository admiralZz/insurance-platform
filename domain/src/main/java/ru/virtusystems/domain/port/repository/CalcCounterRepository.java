package ru.virtusystems.domain.port.repository;

import ru.virtusystems.domain.model.CalcCounter;

import java.time.LocalDate;
import java.util.Optional;

public interface CalcCounterRepository {
    Optional<CalcCounter> findByDayAndProductId(LocalDate day, Long productId);
    CalcCounter save(CalcCounter calcCounter);
}
