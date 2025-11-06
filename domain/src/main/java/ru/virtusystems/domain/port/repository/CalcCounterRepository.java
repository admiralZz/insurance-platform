package ru.virtusystems.domain.port.repository;

import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.Product;

import java.time.LocalDate;
import java.util.Optional;

public interface CalcCounterRepository {
    Optional<CalcCounter> findByDayAndProductId(LocalDate day, Long productId);
    CalcCounter save(CalcCounter calcCounter);
    CalcCounter increaseAndGet(LocalDate day, Product product);
}
