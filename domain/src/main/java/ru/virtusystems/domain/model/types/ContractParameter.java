package ru.virtusystems.domain.model.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractParameter {
    private String name;
    private String code;
    private String dictValue;
    private Object inValue;
    private Object calcValue;
    private Object finalValue;

}
