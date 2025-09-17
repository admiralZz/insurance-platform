package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.ContractNumberCounter;
import ru.virtusystems.platform.database.model.CalcCounterEntity;
import ru.virtusystems.platform.database.model.ContractNumberCounterEntity;

@Mapper(componentModel = "spring")
public interface ContractNumberCounterMapper {

    ContractNumberCounterEntity toEntity(ContractNumberCounter contractNumberCounter);
    ContractNumberCounter toDomain(ContractNumberCounterEntity contractNumberCounter);
}
