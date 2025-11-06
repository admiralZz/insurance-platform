package ru.virtusystems.domain.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.generator.CalcGenerator;
import ru.virtusystems.domain.port.repository.CalcCounterRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
public class StandardCalcIdGenerator implements CalcGenerator {

    // TODO Убрать в другое место
    private final CalcCounterRepository repository;

    @Override
    public String generateCalcId(Product product) {
        LocalDate today = LocalDate.now();

        CalcCounter calcCounter = repository.increaseAndGet(today, product);

        // форматируем ID
        String datePart = today.format(DateTimeFormatter.ofPattern("MMdd"));
        return datePart + "_" + String.format("%06d", calcCounter.getCounter());
    }
}
