package ru.virtusystems.domain.port;


import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;

public interface CalcValidateService {
    void validate(DmsCalculateRequest calculateRequest) throws Exception;
}
