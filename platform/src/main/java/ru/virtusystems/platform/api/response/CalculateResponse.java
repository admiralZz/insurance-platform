package ru.virtusystems.platform.api.response;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class CalculateResponse {
    BigDecimal premium;
    BigDecimal insuranceSum;
}
