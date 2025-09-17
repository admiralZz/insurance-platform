package ru.virtusystems.platform.database.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.ContractNumberCounter;
import ru.virtusystems.domain.port.repository.CalcCounterRepository;
import ru.virtusystems.domain.port.repository.ContractNumberCounterRepository;
import ru.virtusystems.platform.database.model.CalcCounterEntity;
import ru.virtusystems.platform.database.model.ContractNumberCounterEntity;
import ru.virtusystems.platform.database.repository.CalcCounterEntityRepository;
import ru.virtusystems.platform.database.repository.ContractNumberCounterEntityRepository;
import ru.virtusystems.platform.mapper.CalcCounterMapper;
import ru.virtusystems.platform.mapper.ContractNumberCounterMapper;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractNumberCounterRepositoryJpaAdapter implements ContractNumberCounterRepository {

    private final ContractNumberCounterEntityRepository contractNumberCounterEntityRepository;
    private final ContractNumberCounterMapper contractNumberCounterMapper;

    @Override
    public Optional<ContractNumberCounter> findById(Long id) {
        return contractNumberCounterEntityRepository.findByIdForUpdate(id)
                .map(contractNumberCounterMapper::toDomain);
    }

    @Override
    public ContractNumberCounter save(ContractNumberCounter counter) {
        ContractNumberCounterEntity entity = contractNumberCounterMapper.toEntity(counter);
        ContractNumberCounterEntity saved = contractNumberCounterEntityRepository.save(entity);

        return contractNumberCounterMapper.toDomain(saved);
    }
}
