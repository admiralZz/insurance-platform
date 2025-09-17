package ru.virtusystems.platform.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.virtusystems.platform.api.request.ProductCalculateRequest;
import ru.virtusystems.platform.api.request.ProductIssueRequest;
import ru.virtusystems.platform.api.request.ProductSaveRequest;
import ru.virtusystems.platform.api.request.ProductUpdateRequest;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.service.ProductService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductDispatcherImpl implements ProductDispatcher {

    private final Map<String, ProductService> productServices;

    @Override
    public ContractResponse calculate(ProductCalculateRequest request) {
        return getService(request.getProduct()).calculate(request);
    }

    @Override
    public ContractResponse save(ProductSaveRequest request) {
        return getService(request.getProduct()).save(request);
    }

    @Override
    public ContractResponse update(ProductUpdateRequest request) {
        return getService(request.getProduct()).update(request);
    }

    @Override
    public ContractResponse issue(ProductIssueRequest request) {
        return getService(request.getProduct()).issue(request);
    }

    private ProductService getService(String productCode) {
        ProductService service = productServices.get(productCode);
        if (service == null) {
            throw new IllegalArgumentException("Unknown product: " + productCode);
        }
        return service;
    }
}
