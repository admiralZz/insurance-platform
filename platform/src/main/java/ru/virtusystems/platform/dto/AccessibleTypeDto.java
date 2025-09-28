package ru.virtusystems.platform.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AccessibleTypeDto {
    String parameterCode;
    Map<String, String> valuesAndCodesMap;
}
