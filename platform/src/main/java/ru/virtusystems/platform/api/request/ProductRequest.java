package ru.virtusystems.platform.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProductRequest {
    @NotBlank
    private String product;
}
