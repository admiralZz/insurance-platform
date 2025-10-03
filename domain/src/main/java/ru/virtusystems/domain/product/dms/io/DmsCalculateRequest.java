package ru.virtusystems.domain.product.dms.io;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.port.validation.ValidatedRequest;

@Value
@Builder
public class DmsCalculateRequest implements ValidatedRequest {
    String program;
    String period;
}
