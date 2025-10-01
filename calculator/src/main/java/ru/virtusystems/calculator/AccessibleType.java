package ru.virtusystems.calculator;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class AccessibleType {
    String name;
    String code;
    Set<AccessibleTypeValue> accessibleTypeValues;

    @Value
    @Builder
    public static class AccessibleTypeValue {
        String code;
        String name;
        boolean accessible;
    }
}


