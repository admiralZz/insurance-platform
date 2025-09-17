package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.port.TariffEvaluator;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.domain.product.dms.io.DmsCalculateResponse;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DmsCalculationService {
    private final TariffEvaluator tariffEvaluator;
    private final DmsContractDatesService contractDatesService;
    private final DmsSettingTablesService settingTablesService;
    private final DmsCalcValidateService dmsCalcValidateService;

    public DmsCalculateResponse calculate(DmsCalculateRequest request) {
        DmsTariffModel dmsTariffModel = calculateTariffModel(request);

        return new DmsCalculateResponse(dmsTariffModel.getTotalPremium(), dmsTariffModel.getInsuranceSum());
    }

    @SneakyThrows
    public DmsTariffModel calculateTariffModel(DmsCalculateRequest calculateRequest) {
        dmsCalcValidateService.validate(calculateRequest);

        LocalDateTime endDate = contractDatesService.endDate(calculateRequest.getPeriod());
        DmsTariffModel.DmsTariffModelBuilder<?, ?> tariffModelBuilder = DmsTariffModel.builder();
        tariffModelBuilder.program(calculateRequest.getProgram())
                .period(calculateRequest.getPeriod())
                .issueDate(LocalDateTime.now())
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(endDate)
                .insuranceSumFromSettings(settingTablesService.getInsuranceSumByProgramName(calculateRequest.getProgram()))
                .premiumFromSettings(settingTablesService.getPremiumByProgramName(calculateRequest.getProgram()));
        DmsTariffModel inputState = tariffModelBuilder
                .build();
        TariffEvaluationState result = tariffEvaluator.evaluateState(TariffEvaluationState.builder()
                .parameters(inputState.buildParametersState())
                .build());

        return tariffModelBuilder
                .parameters(result.getParameters())
                .build();
    }

}
