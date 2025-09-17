package ru.virtusystems.platform.service;

import ru.virtusystems.platform.api.request.ProductCalculateRequest;
import ru.virtusystems.platform.api.request.ProductIssueRequest;
import ru.virtusystems.platform.api.request.ProductSaveRequest;
import ru.virtusystems.platform.api.request.ProductUpdateRequest;
import ru.virtusystems.platform.api.response.ContractResponse;

public interface ProductService {
    ContractResponse calculate(ProductCalculateRequest request);
    ContractResponse save(ProductSaveRequest request);
    ContractResponse update(ProductUpdateRequest request);
    ContractResponse issue(ProductIssueRequest request);
}
