package ru.virtusystems.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.virtusystems.database.model.types.ContractParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@SuperBuilder
public abstract class TariffModel {

    @Builder.Default
    protected final List<ContractParameter> parameters = new ArrayList<>();

    public Object getParameterValueByCode(String code) {
        return getContractParameterByCode(code)
                .map(ContractParameter::getFinalValue)
                .orElseThrow(() -> new IllegalArgumentException("Параметр с кодом '" + code + "' не найден"));
    }

    public Optional<ContractParameter> getContractParameterByCode(String code) {
        return parameters.stream()
                .filter(contractParameter -> contractParameter.getCode().equals(code))
                .findFirst();
    }

    public void addContractParameterByCode(String code, Object value) {
        parameters.add(ContractParameter.builder()
                        .code(code)
                        .inValue(value)
                .build());
    }


}
