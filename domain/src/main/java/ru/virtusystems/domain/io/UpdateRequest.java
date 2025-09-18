package ru.virtusystems.domain.io;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.Insured;

@Value
@Builder
public class UpdateRequest {
    Long policyId;

    CalculateRequest calcRequest;

    Insured insured;
}
