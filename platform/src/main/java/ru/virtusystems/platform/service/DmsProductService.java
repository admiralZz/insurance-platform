package ru.virtusystems.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.product.dms.DmsContractService;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.platform.api.request.ProductCalculateRequest;
import ru.virtusystems.platform.api.request.ProductIssueRequest;
import ru.virtusystems.platform.api.request.ProductSaveRequest;
import ru.virtusystems.platform.api.request.ProductUpdateRequest;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.mapper.InsuredMapper;
import ru.virtusystems.platform.mapper.product.DmsCalculateRequestMapper;

import java.util.Optional;

@Service("ДМС при ДТП")
@RequiredArgsConstructor
public class DmsProductService implements ProductService {
    private final DmsContractService dmsContractService;
    private final DmsCalculateRequestMapper dmsCalculateRequestMapper;
    private final ContractMapper contractMapper;
    private final InsuredMapper insuredMapper;

    @Transactional
    public ContractResponse calculate(ProductCalculateRequest productCalculateRequest) {
        Contract contract = dmsContractService
                .calculate(dmsCalculateRequestMapper.toDmsCalculateRequest(productCalculateRequest.getCalc()));

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    @Transactional
    public ContractResponse save(ProductSaveRequest productSaveRequest) {
        DmsCalculateRequest dmsCalculateRequest = Optional.ofNullable(productSaveRequest.getCalcRequest())
                .map(ProductCalculateRequest::getCalc)
                .map(dmsCalculateRequestMapper::toDmsCalculateRequest)
                .orElse(null);
        SaveRequest<DmsCalculateRequest> saveRequest = SaveRequest.<DmsCalculateRequest>builder()
                .calcRequest(dmsCalculateRequest)
                .insured(insuredMapper.toDomain(productSaveRequest.getInsured()))
                .build();
        Contract contract = dmsContractService.save(saveRequest);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    @Transactional
    public ContractResponse update(ProductUpdateRequest productUpdateRequest) {
        DmsCalculateRequest dmsCalculateRequest = Optional.ofNullable(productUpdateRequest.getCalcRequest())
                .map(ProductCalculateRequest::getCalc)
                .map(dmsCalculateRequestMapper::toDmsCalculateRequest)
                .orElse(null);
        UpdateRequest<DmsCalculateRequest> updateRequest = UpdateRequest.<DmsCalculateRequest>builder()
                .calcRequest(dmsCalculateRequest)
                .insured(insuredMapper.toDomain(productUpdateRequest.getInsured()))
                .build();
        Contract contract = dmsContractService.update(updateRequest);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }

    @Transactional
    public ContractResponse issue(ProductIssueRequest productIssueRequest) {
        IssueRequest issueRequest = IssueRequest.builder()
                .policyId(productIssueRequest.getPolicyId())
                .build();
        Contract contract = dmsContractService.issue(issueRequest);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contract))
                .build();
    }
}
