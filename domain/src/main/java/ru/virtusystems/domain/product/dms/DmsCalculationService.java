package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.virtusystems.domain.calculation.CalculationService;
import ru.virtusystems.domain.contract.BaseTariffModel;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.port.TariffEvaluator;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.domain.product.dms.io.DmsCalculateResponse;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;
import ru.virtusystems.domain.validation.ValidatedRequest;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DmsCalculationService implements CalculationService {
    private final TariffEvaluator tariffEvaluator;
    private final DmsContractDatesService contractDatesService;
    private final DmsSettingTablesService settingTablesService;
    private final DmsCalcValidateService dmsCalcValidateService;

    public DmsCalculateResponse calculate(ValidatedRequest request) {
        BaseTariffModel tariffModel = calculateTariffModel(request);

        return new DmsCalculateResponse(tariffModel.getTotalPremium(), tariffModel.getInsuranceSum());
    }

    @SneakyThrows
    @Override
    public BaseTariffModel calculateTariffModel(ValidatedRequest calculateRequest) {
        dmsCalcValidateService.validate(calculateRequest);
        DmsCalculateRequest dmsCalculateRequest = (DmsCalculateRequest) calculateRequest;

        LocalDateTime endDate = contractDatesService.endDate(dmsCalculateRequest.getPeriod());
        DmsTariffModel.DmsTariffModelBuilder<?, ?> tariffModelBuilder = DmsTariffModel.builder();
        tariffModelBuilder.program(dmsCalculateRequest.getProgram())
                .period(dmsCalculateRequest.getPeriod())
                .issueDate(LocalDateTime.now())
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(endDate)
                .insuranceSumFromSettings(settingTablesService.getInsuranceSumByProgramName(
                        dmsCalculateRequest.getProgram()))
                .premiumFromSettings(settingTablesService.getPremiumByProgramName(
                        dmsCalculateRequest.getProgram()));
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
