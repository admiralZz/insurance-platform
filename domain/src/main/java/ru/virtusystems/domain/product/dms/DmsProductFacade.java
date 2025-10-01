package ru.virtusystems.domain.product.dms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.contract.PartnerContractService;
import ru.virtusystems.domain.contract.OfficeContractService;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.product.ProductFacade;
import ru.virtusystems.domain.product.ProductService;

@Getter
@RequiredArgsConstructor
public class DmsProductFacade implements ProductFacade {
    public static final String PRODUCT_NAME = "ДМС при ДТП";

    private final ProductService productService;
    private final PartnerContractService partnerContractService;
    private final OfficeContractService officeContractService;
    private Product product;

    @Override
    public Product create() {
        product = productService.getOrCreate(Product.builder()
                .name(PRODUCT_NAME)
                .description("Продукт страхования дохода при инвестиционных рисках")
                .build());

        return product;
    }

}
