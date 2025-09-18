package ru.virtusystems.platform.api.request;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.virtusystems.platform.dto.CreateInsuredDto;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductUpdateRequest extends ProductRequest {
    Long policyId;

    @Nullable
    Map<String, Object> calc;

    @Valid
    @Nullable
    CreateInsuredDto insured;
}
