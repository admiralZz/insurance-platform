package ru.virtusystems.domain.port.product;

import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.domain.port.contract.OfficeContractService;
import ru.virtusystems.domain.model.Product;

public interface ProductFacade {
    Product create();
    PartnerContractService getPartnerContractService();
    OfficeContractService getOfficeContractService();
}
