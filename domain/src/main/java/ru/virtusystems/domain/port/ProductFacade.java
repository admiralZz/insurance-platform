package ru.virtusystems.domain.port;

public interface ProductFacade<T extends CalculateRequest> {
    ContractResponse calculate(T request);
    ContractResponse save(T request);
    ContractResponse update(T request);
    ContractResponse issue(T request);
}
