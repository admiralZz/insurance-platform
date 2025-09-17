package ru.virtusystems.domain;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.port.CalcValidateService;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;

@RequiredArgsConstructor
public class StandardCalcValidateService implements CalcValidateService {

    @Override
    public void validate(DmsCalculateRequest calculateRequest) throws Exception {

    }
}
