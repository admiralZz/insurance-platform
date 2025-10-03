package ru.virtusystems.domain.dictionary.territory;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.territory.Country;
import ru.virtusystems.domain.port.territory.CountryService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class StandardCountryService implements CountryService {

    private static final String LANGUAGE = "ru";

    @Override
    public List<Country> getCountries() {
        Locale russian = new Locale(LANGUAGE);
        return Arrays.stream(Locale.getISOCountries())
                .map(code -> {
                    Locale locale = new Locale(LANGUAGE, code);
                    return Country.builder()
                            .code(code)
                            .name(locale.getDisplayCountry(russian))
                            .build();
                })
                .sorted(Comparator.comparing(Country::getName))
                .toList();
    }

}
