package ru.virtusystems.domain.port.validation;


import ru.virtusystems.domain.model.Contract;

public interface IssueValidateService {
    void validate(Contract contract) throws Exception;
}
