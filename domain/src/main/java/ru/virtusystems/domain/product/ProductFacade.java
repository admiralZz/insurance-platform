package ru.virtusystems.domain.product;

import ru.virtusystems.domain.contract.ContractService;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.AccessibleTypesCollector;

public interface ProductFacade {
    Product create();
    ContractService getContractService();
    AccessibleTypesCollector getAccessibleTypesCollector();
}
