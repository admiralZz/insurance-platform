package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.virtusystems.domain.model.CalcCounter;
import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.platform.database.model.CalcCounterEntity;
import ru.virtusystems.platform.database.model.InsuredEntity;
import ru.virtusystems.platform.dto.CreateInsuredDto;
import ru.virtusystems.platform.dto.ReadInsuredDto;

@Mapper(componentModel = "spring")
public interface CalcCounterMapper {

    CalcCounterEntity toEntity(CalcCounter calcCounter);
    CalcCounter toDomain(CalcCounterEntity calcCounterEntity);
}
