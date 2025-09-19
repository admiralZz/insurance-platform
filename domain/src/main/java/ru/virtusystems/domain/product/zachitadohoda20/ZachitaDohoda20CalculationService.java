package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.port.TariffEvaluator;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateRequest;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateResponse;
import ru.virtusystems.domain.product.zachitadohoda20.model.ZachitaDohoda20TariffModel;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ZachitaDohoda20CalculationService {
    private final TariffEvaluator tariffEvaluator;
    private final ZachitaDohoda20ContractDatesService contractDatesService;
    private final ZachitaDohoda20SettingTablesService settingTablesService;
    private final ZachitaDohoda20CalcValidateService dmsCalcValidateService;

    public ZachitaDohoda20CalculateResponse calculate(ZachitaDohoda20CalculateRequest request) {
        ZachitaDohoda20TariffModel zachitaDohoda20TariffModel = calculateTariffModel(request);

        return new ZachitaDohoda20CalculateResponse(
                zachitaDohoda20TariffModel.getTotalPremium(),
                zachitaDohoda20TariffModel.getInsuranceSum());
    }

    @SneakyThrows
    public ZachitaDohoda20TariffModel calculateTariffModel(ZachitaDohoda20CalculateRequest calculateRequest) {
        dmsCalcValidateService.validate(calculateRequest);

        LocalDateTime endDate = contractDatesService.endDate(calculateRequest.getPeriod());
        ZachitaDohoda20TariffModel.ZachitaDohoda20TariffModelBuilder<?, ?> tariffModelBuilder =
                ZachitaDohoda20TariffModel.builder();
        tariffModelBuilder.program(calculateRequest.getProgram())
                .period(calculateRequest.getPeriod())
                .issueDate(LocalDateTime.now())
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(endDate)
                .insuranceSumFromSettings(settingTablesService.getInsuranceSumByProgramNameAndPeriod(calculateRequest.getProgram(), calculateRequest.getPeriod()))
                .premiumFromSettings(settingTablesService.getPremiumByProgramNameAndPeriod(calculateRequest.getProgram(), calculateRequest.getPeriod()))
                .accessiblePaymentMethodFromSettings(settingTablesService.getAccessiblePaymentMethod())
                .accessiblePeriodFromSettings(settingTablesService.getAccessiblePeriodDays())
                .accessibleProgramFromSettings(settingTablesService.getAccessibleProgram());
        ZachitaDohoda20TariffModel inputState = tariffModelBuilder
                .build();
        TariffEvaluationState result = tariffEvaluator.evaluateState(TariffEvaluationState.builder()
                .parameters(inputState.buildParametersState())
                .build());

        return tariffModelBuilder
                .parameters(result.getParameters())
                .build();
    }

}
