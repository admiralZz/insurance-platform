package ru.virtusystems.platform.database.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.port.repository.CalcCounterRepository;
import ru.virtusystems.platform.database.model.CalcCounterEntity;
import ru.virtusystems.platform.database.repository.CalcCounterEntityRepository;
import ru.virtusystems.platform.mapper.CalcCounterMapper;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CalcCounterRepositoryJpaAdapter implements CalcCounterRepository {

    private final CalcCounterEntityRepository calcCounterEntityRepository;
    private final CalcCounterMapper calcCounterMapper;


    @Override
    public Optional<CalcCounter> findByDayAndProductId(LocalDate day, Long productId) {
        return calcCounterEntityRepository.findByDayAndProductIdForUpdate(day, productId)
                .map(calcCounterMapper::toDomain);
    }

    @Override
    public CalcCounter save(CalcCounter calcCounter) {
        CalcCounterEntity entity = calcCounterMapper.toEntity(calcCounter);
        CalcCounterEntity saved = calcCounterEntityRepository.save(entity);

        return calcCounterMapper.toDomain(saved);
    }
}
