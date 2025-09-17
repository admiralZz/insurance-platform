package ru.virtusystems.platform.database.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.repository.ContractEntityRepository;
import ru.virtusystems.platform.mapper.ContractMapper;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryJpaAdapter implements ContractRepository {

    private final ContractEntityRepository entityRepository;
    private final ContractMapper contractMapper;

    @Override
    public Contract save(Contract contract) {
        ContractEntity entity = contractMapper.toEntity(contract);
        ContractEntity saved = entityRepository.save(entity);
        return contractMapper.toDomain(saved);
    }

    @Override
    public Optional<Contract> findById(Long id) {
        return entityRepository.findById(id)
                .map(contractMapper::toDomain);
    }
}
