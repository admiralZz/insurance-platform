package ru.virtusystems.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.platform.api.request.ProductCalculateRequest;
import ru.virtusystems.platform.api.request.ProductIssueRequest;
import ru.virtusystems.platform.api.request.ProductSaveRequest;
import ru.virtusystems.platform.api.request.ProductUpdateRequest;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.mapper.InsuredMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StandardProductDispatcherService implements ProductDispatcher {

    private final ProductCollector productCollector;
    private final ContractMapper contractMapper;
    private final InsuredMapper insuredMapper;

    @Override
    @Transactional
    public ContractResponse calculate(ProductCalculateRequest calculateRequest) {
        PartnerContractService partnerContractService = productCollector.getContractService(calculateRequest);

        Contract contract = partnerContractService
                .calculate(CalculateRequest.builder()
                        .calcId(calculateRequest.getCalcId())
                        .calc(calculateRequest.getCalc())
                        .build());

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    @Override
    @Transactional
    public ContractResponse save(ProductSaveRequest saveRequest) {
        PartnerContractService partnerContractService = productCollector.getContractService(saveRequest);

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
        PartnerContractService partnerContractService = productCollector.getContractService(updateRequest);

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
    public ContractResponse issue(ProductIssueRequest productIssueRequest) throws Exception {
        PartnerContractService partnerContractService = productCollector.getContractService(productIssueRequest);

        IssueRequest issueRequest = IssueRequest.builder()
                .policyId(productIssueRequest.getPolicyId())
                .build();
        Contract contract = partnerContractService.issue(issueRequest);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }
}
