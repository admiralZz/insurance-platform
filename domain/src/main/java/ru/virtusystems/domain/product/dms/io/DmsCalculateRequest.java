package ru.virtusystems.domain.product.dms.io;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DmsCalculateRequest {
    String program;
    String period;
}
