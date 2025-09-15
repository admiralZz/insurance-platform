package ru.virtusystems.platform.api.request;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Value;
import ru.virtusystems.dto.CreateInsuredDto;

@Value
@Builder
public class UpdateRequest {
    Long policyId;

    @Valid
    @Nullable
    CalculateRequest calcRequest;

    @Valid
    @Nullable
    CreateInsuredDto insured;
}
