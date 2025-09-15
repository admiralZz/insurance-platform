package ru.virtusystems.calculator;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.database.model.types.ContractParameter;

import java.util.List;

@Value
@Builder
public class TariffEvaluationState {
    List<ContractParameter> parameters;
}
