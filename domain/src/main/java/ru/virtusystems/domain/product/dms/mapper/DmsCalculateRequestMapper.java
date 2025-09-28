package ru.virtusystems.domain.product.dms.mapper;

import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.mapper.CalculateRequestMapper;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;

import java.util.Map;
import java.util.Optional;

public class DmsCalculateRequestMapper implements CalculateRequestMapper {

    @Override
    public DmsCalculateRequest map(CalculateRequest calculateRequest) {
        Map<String, Object> calc = calculateRequest.getCalc();

        return DmsCalculateRequest.builder()
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
