package ru.virtusystems.domain.port;

import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;

import java.util.Map;

public interface TariffDescriptor {
    Map<String, Map<String, String>> getAccessibleTypesMap(TariffEvaluationState inputState);
    TariffEvaluationState evaluateState(TariffEvaluationState inputState);
    Map<String, String> getAccessibleTypesByCode(String code);
}
