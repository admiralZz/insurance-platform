package ru.virtusystems.domain.port.contract;

import java.util.Map;

public interface OfficeContractService {
    Map<String, Map<String, String>> getAccessibleTypesMap();
}
