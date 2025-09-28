package ru.virtusystems.domain.calculation;

import ru.virtusystems.domain.model.evaluator.TariffModel;
import ru.virtusystems.domain.validation.ValidatedRequest;

public interface CalculationService {
    TariffModel calculateTariffModel(ValidatedRequest calculateRequest);
}
