package ru.virtusystems.platform.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.domain.port.product.ProductFacade;
import ru.virtusystems.platform.api.request.ProductRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandardProductCollector implements ProductCollector {
    private final List<ProductFacade> productServices;

    @PostConstruct
    @Transactional
    public void init() {
        productServices.forEach(ProductFacade::create);
    }

    @Override
    public PartnerContractService getContractService(ProductRequest productRequest) {
        String productName = Optional.ofNullable(productRequest.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Не указан продукт"));

        ProductFacade productFacade = findProductFacadeByName(productName);
        PartnerContractService partnerContractService = productFacade.getPartnerContractService();
        if (partnerContractService == null) {
            throw new IllegalArgumentException("Сервис оформления договоров для продукта '"
                    + productName + "' не определен");
        }

        return partnerContractService;
    }

    @Override
    public ProductFacade getProductFacadeByName(String productName) {
        return findProductFacadeByName(productName);
    }

    private ProductFacade findProductFacadeByName(String productName) {
        return productServices.stream()
                .filter(facade -> facade.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: " + productName));
    }
}
