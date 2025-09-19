package ru.virtusystems.domain.product.zachitadohoda20.io;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ZachitaDohoda20CalculateResponse {
    BigDecimal premium;
    BigDecimal insuranceSum;
}
