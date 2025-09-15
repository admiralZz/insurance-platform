package ru.virtusystems.platform.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CalculateRequest {
    @NotBlank
    String program;
    @NotBlank
    String period;
}
