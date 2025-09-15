package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.database.model.CalcCounter;
import ru.virtusystems.database.repository.CalcCounterRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DmsCalcIdGenerator {

    private final CalcCounterRepository repository;

    @Transactional
    public String generateCalcId() {
        LocalDate today = LocalDate.now();

        // читаем строку с блокировкой
        CalcCounter counter = repository.findByDayForUpdate(today)
                .orElseGet(() -> {
                    CalcCounter newCounter = new CalcCounter();
                    newCounter.setDay(today);
                    newCounter.setCounter(0L);
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
