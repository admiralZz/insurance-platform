package ru.virtusystems.domain.port.repository;

import ru.virtusystems.domain.model.ContractNumberCounter;

import java.util.Optional;

public interface ContractNumberCounterRepository {
    Optional<ContractNumberCounter> findById(Long id);
    ContractNumberCounter save(ContractNumberCounter counter);
}
