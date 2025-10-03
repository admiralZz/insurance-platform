package ru.virtusystems.domain.mapper;

import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.port.validation.ValidatedRequest;

public interface CalculateRequestMapper {
    // Для преобразования сущности из одной в другую
    ValidatedRequest map(CalculateRequest calculatedRequest);
}
