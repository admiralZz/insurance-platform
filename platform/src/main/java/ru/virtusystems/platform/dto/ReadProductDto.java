package ru.virtusystems.platform.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadProductDto {
    String name;
    String description;
}
