package ru.virtusystems.domain.product.dms;

import ru.virtusystems.domain.port.dates.ContractDatesService;

import java.time.LocalDateTime;
import java.util.Map;

public class DmsContractDatesService implements ContractDatesService {

    @Override
    public LocalDateTime startDate() {
        return LocalDateTime.now().plusDays(1);
    }

    @Override
    public LocalDateTime endDate(String period) {
        if (period == null) {
            return null;
        }
        Map<String, Long> map = Map.of("1 год", 1L,
                "2 года", 2L,
                "3 года", 3L,
                "4 года", 4L,
                "5 лет", 5L,
                "6 лет", 6L,
                "7 лет", 7L);
        Long years = map.get(period);
        return LocalDateTime.now().plusYears(years);
    }
}
