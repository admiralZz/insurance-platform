package ru.virtusystems.platform.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.port.product.ProductFacade;
import ru.virtusystems.platform.api.request.*;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.mapper.InsuredMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StandardProductDispatcherService implements ProductDispatcher {

    private final List<ProductFacade> productServices;
    private final ContractMapper contractMapper;
    private final InsuredMapper insuredMapper;

    @PostConstruct
    @Transactional
    public void init() {
        // TODO перенести в отдельный коллектор продуктов
        productServices.forEach(ProductFacade::create);
    }

    @Override
    @Transactional
    public ContractResponse calculate(ProductCalculateRequest calculateRequest) {
        PartnerContractService partnerContractService = getService(calculateRequest);

        Contract contract = partnerContractService
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
        PartnerContractService partnerContractService = getService(saveRequest);

        CalculateRequest calculateRequest = Optional.ofNullable(saveRequest.getCalc())
                .map(calc -> CalculateRequest.builder()
                        .calc(calc)
                        .build())
                .orElse(null);
        Contract contract = partnerContractService.save(SaveRequest.builder()
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
        PartnerContractService partnerContractService = getService(updateRequest);

        CalculateRequest calculateRequest = Optional.ofNullable(updateRequest.getCalc())
                .map(calc -> CalculateRequest.builder()
                        .calc(calc)
                        .build())
                .orElse(null);
        Contract contract = partnerContractService.update(UpdateRequest.builder()
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
        PartnerContractService partnerContractService = getService(productIssueRequest);

        IssueRequest issueRequest = IssueRequest.builder()
                .policyId(productIssueRequest.getPolicyId())
                .build();
        Contract contract = partnerContractService.issue(issueRequest);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    // TODO вынести в отдельный коллектор продуктов
    private PartnerContractService getService(ProductRequest productRequest) {
        String productName = Optional.ofNullable(productRequest.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Не указан продукт"));

        ProductFacade productFacade = productServices.stream()
                .filter(facade -> facade.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: " + productName));
        PartnerContractService partnerContractService = productFacade.getPartnerContractService();
        if (partnerContractService == null) {
            throw new IllegalArgumentException("Сервис оформления договоров для продукта '"
                    + productName + "' не определен");
        }

        return partnerContractService;
    }
}
