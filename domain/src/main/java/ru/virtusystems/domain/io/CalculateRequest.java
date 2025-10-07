package ru.virtusystems.domain.io;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class CalculateRequest {
    String calcId;
    Map<String, Object> calc;
}
