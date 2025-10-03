package ru.virtusystems.domain.validation;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.port.validation.CalcValidateService;
import ru.virtusystems.domain.port.validation.ValidatedRequest;

@RequiredArgsConstructor
public class StandardCalcValidateService implements CalcValidateService {

    @Override
    public void validate(ValidatedRequest calculateRequest) throws Exception {

    }
}
