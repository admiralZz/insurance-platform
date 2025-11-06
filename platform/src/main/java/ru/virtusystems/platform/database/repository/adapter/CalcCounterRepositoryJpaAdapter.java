package ru.virtusystems.platform.database.repository.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.repository.CalcCounterRepository;
import ru.virtusystems.platform.database.model.CalcCounterEntity;
import ru.virtusystems.platform.database.repository.CalcCounterEntityRepository;
import ru.virtusystems.platform.mapper.CalcCounterMapper;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CalcCounterRepositoryJpaAdapter implements CalcCounterRepository {

    private final CalcCounterEntityRepository calcCounterEntityRepository;
    private final CalcCounterMapper calcCounterMapper;

    @Override
    public Optional<CalcCounter> findByDayAndProductId(LocalDate day, Long productId) {
        log.debug("Looking for product with productId={} and date={}", productId, day);
        return calcCounterEntityRepository.findByDayAndProductIdForUpdate(day, productId)
                .map(calcCounterMapper::toDomain);
    }

    @Override
    public CalcCounter save(CalcCounter calcCounter) {
        log.debug("Saving calc counter: {}", calcCounter);
        CalcCounterEntity entity = calcCounterMapper.toEntity(calcCounter);
        CalcCounterEntity saved = calcCounterEntityRepository.save(entity);

        return calcCounterMapper.toDomain(saved);
    }

    /*
        Была проблема при многопоточности - т.к. транзакция создания договора довольно длинная, то
        при генерации calcId возникал конфликт создания нового первичного ключа(день + продукт) для текущего дня.
        Пессимистичная блокировка блокирует только существующие записи, поэтому при создании новой записи это не спасало.
     */
    @Override
    public CalcCounter increaseAndGet(LocalDate day, Product product) {
        log.debug("Getting calc counter for {}", product);

        // Пытаемся создать запись атомарно
        int inserted = calcCounterEntityRepository.insertIfNotExists(day, product.getId());

        // inserted = 1 если была создана запись
        if (inserted > 0) {
            log.debug("Created new calc counter for day {} and product {}", day, product.getId());
        }

        // Теперь запись точно существует, можем делать SELECT FOR UPDATE
        CalcCounterEntity calcCounterEntity = calcCounterEntityRepository
                .findByDayAndProductIdForUpdate(day, product.getId())
                .orElseThrow(() -> new IllegalStateException("Counter must exist"));

        calcCounterEntity.setCounter(calcCounterEntity.getCounter() + 1);
        log.debug("Increasing calc counter {}", calcCounterEntity.getCounter());

        return calcCounterMapper.toDomain(calcCounterEntityRepository.save(calcCounterEntity));
    }
}
