package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.dto.ReadContractDto;

@Mapper(componentModel = "spring", uses = {InsuredMapper.class, ProductMapper.class})
public interface ContractMapper {

    ReadContractDto toDto(Contract contract);

    ReadContractDto toDto(ContractEntity contractEntity);

    ContractEntity toEntity(ReadContractDto readContractDto);

    ContractEntity toEntity(Contract contract);

    Contract toDomain(ContractEntity contractEntity);
}
