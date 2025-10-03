package ru.virtusystems.domain.port.validation;


public interface CalcValidateService {
    void validate(ValidatedRequest validatedRequest) throws Exception;
}
