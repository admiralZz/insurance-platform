package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.port.TariffDescriptor;
import ru.virtusystems.domain.product.zachitadohoda20.model.ZachitaDohoda20TariffModel;
import ru.virtusystems.domain.validation.StandardCalcValidateService;
import ru.virtusystems.domain.port.validation.ValidatedRequest;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateRequest;

import java.util.Map;

@RequiredArgsConstructor
public class ZachitaDohoda20CalcValidateService extends StandardCalcValidateService {

    private final TariffDescriptor tariffDescriptor;

    @Override
    public void validate(ValidatedRequest calculateRequest) throws Exception {
        super.validate(calculateRequest);
        ZachitaDohoda20CalculateRequest zachitaDohoda20CalculateRequest =
                (ZachitaDohoda20CalculateRequest) calculateRequest;

        checkProgram(zachitaDohoda20CalculateRequest.getProgram());
        checkPeriod(zachitaDohoda20CalculateRequest.getPeriod());

    }

    private void checkProgram(String program) {
        Map<String, String> programs = tariffDescriptor.getAccessibleTypesByCode(ZachitaDohoda20TariffModel.IN_PARAM_PROGRAM);
        if (!programs.containsKey(program)) {
            throw new RuntimeException("Такой программы '" + program + "' не существует");
        }
    }

    private void checkPeriod(String period) {
        Map<String, String> periods = tariffDescriptor.getAccessibleTypesByCode(ZachitaDohoda20TariffModel.IN_PARAM_PERIOD);
        if (!periods.containsKey(period)) {
            throw new RuntimeException("Такой период '" + period + "' не доступен");
        }
    }
}
