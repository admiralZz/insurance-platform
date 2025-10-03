package ru.virtusystems.calculator.port;


import org.apache.poi.ss.usermodel.Workbook;
import ru.virtusystems.calculator.IOParameter;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;

import java.util.List;

public interface TariffEvaluator {
    TariffEvaluationState evaluateState(Workbook workbook, TariffEvaluationState inputState);
    void setParams(Workbook workbook, List<IOParameter> inputParameters);
}
