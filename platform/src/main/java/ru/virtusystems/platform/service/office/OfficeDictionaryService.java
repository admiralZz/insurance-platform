package ru.virtusystems.platform.service.office;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.virtusystems.domain.port.territory.CountryService;
import ru.virtusystems.platform.dto.CountryDto;
import ru.virtusystems.platform.mapper.CountryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeDictionaryService {

    private final CountryService countryService;
    private final CountryMapper countryMapper;

    public List<CountryDto> getAllCountries() {
        return countryService.getCountries()
                .stream()
                .map(countryMapper::toDto)
                .toList();
    }
}
