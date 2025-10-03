package ru.virtusystems.domain.port.calculation;

import ru.virtusystems.domain.model.evaluator.TariffModel;
import ru.virtusystems.domain.port.validation.ValidatedRequest;

public interface CalculationService {
    TariffModel calculateTariffModel(ValidatedRequest calculateRequest);
}
