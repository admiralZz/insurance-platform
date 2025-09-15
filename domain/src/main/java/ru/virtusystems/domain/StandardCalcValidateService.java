package ru.virtusystems.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.virtusystems.api.request.DmsCalculateRequest;
import ru.virtusystems.service.port.CalcValidateService;

@Service
@RequiredArgsConstructor
public class StandardCalcValidateService implements CalcValidateService {

    @Override
    public void validate(DmsCalculateRequest calculateRequest) throws Exception {

    }
}
