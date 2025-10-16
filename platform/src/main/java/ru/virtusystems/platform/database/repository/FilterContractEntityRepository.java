package ru.virtusystems.platform.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.dto.filter.ContractFilter;

import java.util.List;

public interface FilterContractEntityRepository {
    List<ContractEntity> findContractsByFilter(ContractFilter filter);
    Page<ContractEntity> findContractsByFilter(ContractFilter filter, Pageable pageable);
}
