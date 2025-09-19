package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.repository.CalcCounterRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class ZachitaDohoda20CalcIdGenerator {

    // TODO Убрать в другое место
    private final CalcCounterRepository repository;

    public String generateCalcId(Product product) {
        LocalDate today = LocalDate.now();

        CalcCounter counter = repository.findByDayAndProductId(today, product.getId())
                .orElseGet(() -> {
                    CalcCounter newCounter = new CalcCounter();
                    newCounter.setDay(today);
                    newCounter.setCounter(0L);
                    newCounter.setProduct(product);
                    return repository.save(newCounter);
                });

        // увеличиваем
        counter.setCounter(counter.getCounter() + 1);
        repository.save(counter);

        // форматируем ID
        String datePart = today.format(DateTimeFormatter.ofPattern("MMdd"));
        return datePart + "_" + String.format("%06d", counter.getCounter());
    }
}
