package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.virtusystems.api.request.DmsCalculateRequest;
import ru.virtusystems.domain.port.AccessibleTypesCollector;
import ru.virtusystems.dto.DmsTariffModel;
import ru.virtusystems.service.StandardCalcValidateService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DmsCalcValidateService extends StandardCalcValidateService {

    private final AccessibleTypesCollector accessibleTypesCollector;

    @Override
    public void validate(DmsCalculateRequest calculateRequest) throws Exception{
        super.validate(calculateRequest);

        checkProgram(calculateRequest.getProgram());
        checkPeriod(calculateRequest.getPeriod());

    }

    private void checkProgram(String program) {
        Map<String, String> programs = accessibleTypesCollector.getAccessibleTypesByCode(DmsTariffModel.IN_PARAM_PROGRAM);
        if (!programs.containsKey(program)) {
            throw new RuntimeException("Такой программы '" + program + "' не существует");
        }
    }

    private void checkPeriod(String period) {
        Map<String, String> periods = accessibleTypesCollector.getAccessibleTypesByCode(DmsTariffModel.IN_PARAM_PERIOD);
        if (!periods.containsKey(period)) {
            throw new RuntimeException("Такой период '" + period + "' не доступен");
        }
    }
}
