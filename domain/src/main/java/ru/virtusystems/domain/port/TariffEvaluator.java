package ru.virtusystems.domain.port;

import ru.virtusystems.calculator.TariffEvaluationState;

public interface TariffEvaluator {
    TariffEvaluationState evaluateState(TariffEvaluationState inputState);
}
