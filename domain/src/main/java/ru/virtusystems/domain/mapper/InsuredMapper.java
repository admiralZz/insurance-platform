package ru.virtusystems.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.virtusystems.domain.model.Insured;

@Mapper
public interface InsuredMapper {

    @Mapping(target = "id", ignore = true) // ID не должен меняться
    void updateInsured(Insured source, @MappingTarget Insured target);
}
