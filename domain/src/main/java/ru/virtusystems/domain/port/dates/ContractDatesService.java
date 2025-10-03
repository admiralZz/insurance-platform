package ru.virtusystems.domain.port.dates;

import java.time.LocalDateTime;

public interface ContractDatesService {
    LocalDateTime startDate();
    LocalDateTime endDate(String periodDays);
}
