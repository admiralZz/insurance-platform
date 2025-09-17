package ru.virtusystems.platform.api.request;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.virtusystems.platform.dto.CreateInsuredDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSaveRequest extends ProductRequest {
    @Valid
    @Nullable
    ProductCalculateRequest calcRequest;

    @Valid
    @Nullable
    CreateInsuredDto insured;
}
