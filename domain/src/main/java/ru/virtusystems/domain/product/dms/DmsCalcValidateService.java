package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.port.TariffDescriptor;
import ru.virtusystems.domain.validation.StandardCalcValidateService;
import ru.virtusystems.domain.port.validation.ValidatedRequest;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;

import java.util.Map;

@RequiredArgsConstructor
public class DmsCalcValidateService extends StandardCalcValidateService {

    private final TariffDescriptor tariffDescriptor;

    @Override
    public void validate(ValidatedRequest calculateRequest) throws Exception{
        super.validate(calculateRequest);
        DmsCalculateRequest dmsCalculateRequest = (DmsCalculateRequest) calculateRequest;

        checkProgram(dmsCalculateRequest.getProgram());
        checkPeriod(dmsCalculateRequest.getPeriod());

    }

    private void checkProgram(String program) {
        Map<String, String> programs = tariffDescriptor.getAccessibleTypesByCode(DmsTariffModel.IN_PARAM_PROGRAM);
        if (!programs.containsKey(program)) {
            throw new RuntimeException("Такой программы '" + program + "' не существует");
        }
    }

    private void checkPeriod(String period) {
        Map<String, String> periods = tariffDescriptor.getAccessibleTypesByCode(DmsTariffModel.IN_PARAM_PERIOD);
        if (!periods.containsKey(period)) {
            throw new RuntimeException("Такой период '" + period + "' не доступен");
        }
    }
}
