package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.virtusystems.domain.model.evaluator.BaseTariffModel;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.port.TariffDescriptor;
import ru.virtusystems.domain.port.calculation.CalculationService;
import ru.virtusystems.domain.port.dates.ContractDatesService;
import ru.virtusystems.domain.port.validation.ValidatedRequest;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateRequest;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateResponse;
import ru.virtusystems.domain.product.zachitadohoda20.model.ZachitaDohoda20TariffModel;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ZachitaDohoda20CalculationService implements CalculationService {
    private final TariffDescriptor tariffDescriptor;
    private final ContractDatesService contractDatesService;
    private final ZachitaDohoda20SettingTablesService settingTablesService;
    private final ZachitaDohoda20CalcValidateService dmsCalcValidateService;

    public ZachitaDohoda20CalculateResponse calculate(ValidatedRequest request) {
        BaseTariffModel tariffModel = calculateTariffModel(request);

        return new ZachitaDohoda20CalculateResponse(
                tariffModel.getTotalPremium(),
                tariffModel.getInsuranceSum());
    }

    @SneakyThrows
    @Override
    public BaseTariffModel calculateTariffModel(ValidatedRequest calculateRequest) {
        dmsCalcValidateService.validate(calculateRequest);
        ZachitaDohoda20CalculateRequest zdcRequest = (ZachitaDohoda20CalculateRequest) calculateRequest;

        LocalDateTime endDate = contractDatesService.endDate(zdcRequest.getPeriod());
        ZachitaDohoda20TariffModel.ZachitaDohoda20TariffModelBuilder<?, ?> tariffModelBuilder =
                ZachitaDohoda20TariffModel.builder();
        tariffModelBuilder.program(zdcRequest.getProgram())
                .period(zdcRequest.getPeriod())
                .issueDate(LocalDateTime.now())
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(endDate)
                .insuranceSumFromSettings(settingTablesService.getInsuranceSumByProgramNameAndPeriod(
                        zdcRequest.getProgram(),
                        zdcRequest.getPeriod()))
                .premiumFromSettings(settingTablesService.getPremiumByProgramNameAndPeriod(
                        zdcRequest.getProgram(),
                        zdcRequest.getPeriod()))
                .accessiblePaymentMethodFromSettings(settingTablesService.getAccessiblePaymentMethod())
                .accessiblePeriodFromSettings(settingTablesService.getAccessiblePeriodDays())
                .accessibleProgramFromSettings(settingTablesService.getAccessibleProgram());
        ZachitaDohoda20TariffModel inputState = tariffModelBuilder
                .build();
        TariffEvaluationState result = tariffDescriptor.evaluateState(TariffEvaluationState.builder()
                .parameters(inputState.buildParametersState())
                .build());

        return tariffModelBuilder
                .parameters(result.getParameters())
                .build();
    }

}
