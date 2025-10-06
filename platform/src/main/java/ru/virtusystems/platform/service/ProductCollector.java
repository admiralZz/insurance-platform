package ru.virtusystems.platform.service;

import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.domain.port.product.ProductFacade;
import ru.virtusystems.platform.api.request.ProductRequest;

public interface ProductCollector {
    PartnerContractService getContractService(ProductRequest productRequest);
    ProductFacade getProductFacadeByName(String productName);
}
