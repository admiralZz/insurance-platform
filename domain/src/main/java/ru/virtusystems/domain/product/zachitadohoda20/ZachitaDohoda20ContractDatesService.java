package ru.virtusystems.domain.product.zachitadohoda20;

import ru.virtusystems.domain.dates.ContractDatesService;

import java.time.LocalDateTime;
import java.util.Map;

public class ZachitaDohoda20ContractDatesService implements ContractDatesService {

    @Override
    public LocalDateTime startDate() {
        return LocalDateTime.now().plusDays(1);
    }

    @Override
    public LocalDateTime endDate(String periodDays) {
        if (periodDays == null) {
            return null;
        }
        String days = periodDays.replaceAll("дней", "")
                .replaceAll("день", "").trim();

        return LocalDateTime.now().plusDays(Long.parseLong(days));
    }
}
