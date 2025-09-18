package ru.virtusystems.domain.io;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.Insured;

@Value
@Builder
public class SaveRequest {
    CalculateRequest calcRequest;

    Insured insured;
}
