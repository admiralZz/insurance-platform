package ru.virtusystems.domain.product.zachitadohoda20.io;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.port.validation.ValidatedRequest;

@Value
@Builder
public class ZachitaDohoda20CalculateRequest implements ValidatedRequest {
    String program;
    String period;
}
