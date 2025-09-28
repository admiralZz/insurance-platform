package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import ru.virtusystems.domain.model.territory.Country;
import ru.virtusystems.platform.dto.CountryDto;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDto toDto(Country country);
}
