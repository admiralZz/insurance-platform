package ru.virtusystems.domain.port;


import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;

public interface TariffEvaluator {
    TariffEvaluationState evaluateState(TariffEvaluationState inputState);
}
