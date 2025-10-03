package ru.virtusystems.domain.contract;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.port.TariffDescriptor;
import ru.virtusystems.domain.port.contract.OfficeContractService;
import ru.virtusystems.domain.port.setting.SettingTablesService;

import java.util.Map;

@RequiredArgsConstructor
public class StandardOfficeContractService implements OfficeContractService {
    private final TariffDescriptor tariffDescriptor;
    private final SettingTablesService settingTablesService;

    @Override
    public Map<String, Map<String, String>> getAccessibleTypesMap() {
        TariffEvaluationState inputState = TariffEvaluationState.builder()
                .parameters(settingTablesService.
                        getDefaultSettingsTables()
                        .buildParametersState())
                .build();
        return tariffDescriptor.getAccessibleTypesMap(inputState);
    }
}
