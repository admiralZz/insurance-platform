package ru.virtusystems.domain.dates;

import java.time.LocalDateTime;

public interface ContractDatesService {
    LocalDateTime startDate();
    LocalDateTime endDate(String periodDays);
}
