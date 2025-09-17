package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.platform.database.model.InsuredEntity;
import ru.virtusystems.platform.dto.CreateInsuredDto;
import ru.virtusystems.platform.dto.ReadInsuredDto;

@Mapper(componentModel = "spring")
public interface InsuredMapper {

    InsuredEntity toEntity(CreateInsuredDto createInsuredDto);
    ReadInsuredDto toDto(InsuredEntity insured);

    @Mapping(target = "id", ignore = true) // ID не должен меняться
    void updateEntityFromDto(CreateInsuredDto createInsuredDto, @MappingTarget InsuredEntity insured);

    InsuredEntity toEntity(Insured insured);
    Insured toDomain(InsuredEntity insuredEntity);
    Insured toDomain(CreateInsuredDto createInsuredDto);
}
