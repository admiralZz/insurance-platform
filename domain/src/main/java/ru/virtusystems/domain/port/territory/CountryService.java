package ru.virtusystems.domain.port.territory;

import ru.virtusystems.domain.model.territory.Country;

import java.util.List;

public interface CountryService {
    List<Country> getCountries();
}
