package ru.virtusystems.domain.product.dms.io;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DmsCalculateResponse {
    BigDecimal premium;
    BigDecimal insuranceSum;
}
