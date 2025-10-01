package ru.virtusystems.domain.contract;

import java.util.Map;

public interface OfficeContractService {
    Map<String, Map<String, String>> getAccessibleTypesMap();
}
