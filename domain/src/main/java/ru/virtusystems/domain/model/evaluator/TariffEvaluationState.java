package ru.virtusystems.domain.model.evaluator;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.types.ContractParameter;

import java.util.List;

@Value
@Builder
public class TariffEvaluationState {
    List<ContractParameter> parameters;
}
