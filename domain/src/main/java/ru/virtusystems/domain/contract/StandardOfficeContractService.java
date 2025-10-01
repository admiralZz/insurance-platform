package ru.virtusystems.domain.contract;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.port.AccessibleTypesCollector;
import ru.virtusystems.domain.setting.SettingTablesService;

import java.util.Map;

@RequiredArgsConstructor
public class StandardOfficeContractService implements OfficeContractService {
    private final AccessibleTypesCollector accessibleTypesCollector;

    @Override
    public Map<String, Map<String, String>> getAccessibleTypesMap() {
        return accessibleTypesCollector.getAccessibleTypesMap();
    }
}
