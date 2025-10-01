package ru.virtusystems.domain.product;

import ru.virtusystems.domain.contract.PartnerContractService;
import ru.virtusystems.domain.contract.OfficeContractService;
import ru.virtusystems.domain.model.Product;

public interface ProductFacade {
    Product create();
    PartnerContractService getPartnerContractService();
    OfficeContractService getOfficeContractService();
}
