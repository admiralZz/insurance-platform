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

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CalcCounterMapper {

    @Mapping(source = "id.day", target = "day")
    CalcCounter toDomain(CalcCounterEntity entity);

    @Mapping(source = "day", target = "id.day")
    @Mapping(source = "product.id", target = "id.productId")
    CalcCounterEntity toEntity(CalcCounter domain);
}
