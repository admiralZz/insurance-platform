package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.virtusystems.database.model.Insured;
import ru.virtusystems.dto.CreateInsuredDto;
import ru.virtusystems.dto.ReadInsuredDto;

@Mapper(componentModel = "spring")
public interface InsuredMapper {

    Insured toEntity(CreateInsuredDto createInsuredDto);
    ReadInsuredDto toDto(Insured insured);

    @Mapping(target = "id", ignore = true) // ID не должен меняться
    void updateEntityFromDto(CreateInsuredDto createInsuredDto, @MappingTarget Insured insured);
}
