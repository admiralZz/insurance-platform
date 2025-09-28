package ru.virtusystems.domain.port;


import ru.virtusystems.domain.model.types.ContractParameter;

import java.util.Map;

public interface AccessibleTypesCollector {
    String getAccessibleType(ContractParameter contractParameter);
    Map<String, String> getAccessibleTypesByCode(String code);
    Map<String, Map<String, String>> getAccessibleTypesMap();
}
