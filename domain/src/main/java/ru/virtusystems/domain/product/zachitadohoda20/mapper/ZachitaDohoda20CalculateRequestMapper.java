package ru.virtusystems.domain.product.zachitadohoda20.mapper;

import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateRequest;

import java.util.Map;
import java.util.Optional;

public class ZachitaDohoda20CalculateRequestMapper {

    public ZachitaDohoda20CalculateRequest toZachitaDohoda20CalculateRequest(CalculateRequest calculateRequest) {
        Map<String, Object> calc = calculateRequest.getCalc();

        return ZachitaDohoda20CalculateRequest.builder()
                .program(getOrNull(calc.get("program")))
                .period(getOrNull(calc.get("period")))
                .build();
    }
    
    private String getOrNull(Object val) {
        return Optional.ofNullable(val)
                .map(Object::toString)
                .orElse(null);
    }
}
