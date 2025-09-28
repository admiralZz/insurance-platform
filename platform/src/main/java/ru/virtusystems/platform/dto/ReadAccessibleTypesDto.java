package ru.virtusystems.platform.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ReadAccessibleTypesDto {
    List<AccessibleTypeDto> accessibleTypes;
}
