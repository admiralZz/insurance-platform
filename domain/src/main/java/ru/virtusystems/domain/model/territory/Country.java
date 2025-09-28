package ru.virtusystems.domain.model.territory;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Country {
    String code;
    String name;
}
