package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import ru.virtusystems.database.model.Contract;
import ru.virtusystems.dto.ReadContractDto;

@Mapper(componentModel = "spring", uses = {InsuredMapper.class })
public interface ContractMapper {

    ReadContractDto toDto(Contract contract);
}
