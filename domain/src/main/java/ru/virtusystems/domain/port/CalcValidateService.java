package ru.virtusystems.domain.port;

import ru.virtusystems.api.request.DmsCalculateRequest;

public interface CalcValidateService {
    void validate(DmsCalculateRequest calculateRequest) throws Exception;
}
