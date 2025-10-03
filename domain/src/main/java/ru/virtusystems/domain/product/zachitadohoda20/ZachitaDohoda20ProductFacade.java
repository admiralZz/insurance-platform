package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.domain.port.contract.OfficeContractService;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.product.ProductFacade;
import ru.virtusystems.domain.port.product.ProductService;

@Getter
@RequiredArgsConstructor
public class ZachitaDohoda20ProductFacade implements ProductFacade {
    private final String name;
    private final String description;
    private final ProductService productService;
    private final PartnerContractService partnerContractService;
    private final OfficeContractService officeContractService;
    private Product product;

    @Override
    public Product create() {
        product = productService.getOrCreate(Product.builder()
                .name(name)
                .description(description)
                .build());

        return product;
    }

}
