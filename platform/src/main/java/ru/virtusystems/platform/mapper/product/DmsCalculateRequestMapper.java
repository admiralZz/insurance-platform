package ru.virtusystems.platform.mapper.product;

import org.springframework.stereotype.Component;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;

import java.util.Map;

@Component
public class DmsCalculateRequestMapper {

    public DmsCalculateRequest toDmsCalculateRequest(Map<String, Object> calc) {
        return DmsCalculateRequest.builder()
                .program(calc.get("program").toString())
                .period(calc.get("period").toString())
                .build();
    }
}
