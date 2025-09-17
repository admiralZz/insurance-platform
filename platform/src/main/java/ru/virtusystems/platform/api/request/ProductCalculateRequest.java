package ru.virtusystems.platform.api.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCalculateRequest extends ProductRequest {
    private Map<String, Object> calc;
}
