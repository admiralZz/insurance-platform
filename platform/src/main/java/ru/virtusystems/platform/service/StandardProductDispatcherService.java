package ru.virtusystems.platform.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.contract.ContractService;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.product.ProductFacade;
import ru.virtusystems.platform.api.request.*;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.mapper.InsuredMapper;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StandardProductDispatcherService implements ProductDispatcher {

    private final Map<String, ProductFacade> productServices;
    private final ContractMapper contractMapper;
    private final InsuredMapper insuredMapper;

    @PostConstruct
    @Transactional
    public void init() {
        // TODO перенести в отдельный коллектор продуктов
        productServices.forEach((name, service) -> service.create());
    }

    @Override
    @Transactional
    public ContractResponse calculate(ProductCalculateRequest calculateRequest) {
        ContractService contractService = getService(calculateRequest);

        Contract contract = contractService
                .calculate(CalculateRequest.builder()
                        .calc(calculateRequest.getCalc())
                        .build());

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    @Override
    @Transactional
    public ContractResponse save(ProductSaveRequest saveRequest) {
        ContractService contractService = getService(saveRequest);

        CalculateRequest calculateRequest = Optional.ofNullable(saveRequest.getCalc())
                .map(calc -> CalculateRequest.builder()
                        .calc(calc)
                        .build())
                .orElse(null);
        Contract contract = contractService.save(SaveRequest.builder()
                .calcRequest(calculateRequest)
                .insured(insuredMapper.toDomain(saveRequest.getInsured()))
                .build());

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();

    }

    @Override
    @Transactional
    public ContractResponse update(ProductUpdateRequest updateRequest) {
        ContractService contractService = getService(updateRequest);

        CalculateRequest calculateRequest = Optional.ofNullable(updateRequest.getCalc())
                .map(calc -> CalculateRequest.builder()
                        .calc(calc)
                        .build())
                .orElse(null);
        Contract contract = contractService.update(UpdateRequest.builder()
                .policyId(updateRequest.getPolicyId())
                .calcRequest(calculateRequest)
                .insured(insuredMapper.toDomain(updateRequest.getInsured()))
                .build());

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    @Override
    @Transactional
    public ContractResponse issue(ProductIssueRequest productIssueRequest) {
        ContractService contractService = getService(productIssueRequest);

        IssueRequest issueRequest = IssueRequest.builder()
                .policyId(productIssueRequest.getPolicyId())
                .build();
        Contract contract = contractService.issue(issueRequest);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    // TODO вынести в отдельный коллектор продуктов
    private ContractService getService(ProductRequest productRequest) {
        String productName = Optional.ofNullable(productRequest.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Не указан продукт"));

        ProductFacade productFacade = productServices.get(productName);
        if (productFacade == null) {
            throw new IllegalArgumentException("Unknown product: " + productName);
        }
        ContractService contractService = productFacade.getContractService();
        if (contractService == null) {
            throw new IllegalArgumentException("Сервис оформления договоров для продукта '"
                    + productName + "' не определен");
        }

        return contractService;
    }
}
