package ru.virtusystems.calculator.mapper;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.calculator.IOParameter;
import ru.virtusystems.domain.model.types.ContractParameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ContractParametersMapper {

    private final Map<String, Map<String, String>> dictionary;

    public List<IOParameter> mapToInputParameters(List<ContractParameter> contractParameters) {
        return contractParameters.stream()
                .map(contractParameter -> new IOParameter(
                                contractParameter.getName(),
                                contractParameter.getCode(),
                                getDictCode(contractParameter),
                                contractParameter.getInValue(),
                                contractParameter.getCalcValue(),
                                contractParameter.getFinalValue()
                        )
                )
                .collect(Collectors.toList());
    }

    public List<ContractParameter> mapToContractParameters(List<IOParameter> outputParameters) {
        return outputParameters.stream()
                .map(ioParameter -> ContractParameter.builder()
                        .name(ioParameter.name())
                        .code(ioParameter.fullCode())
                        .inValue(ioParameter.inValue())
                        .calcValue(ioParameter.calcValue())
                        .finalValue(ioParameter.finalValue())
                        .build())
                .toList();
    }

    private String getDictCode(ContractParameter contractParameter) {
        return Optional.ofNullable(dictionary.get(contractParameter.getCode()))
                .map(map -> map.get((String) contractParameter.getInValue()))
                .orElse(null);
    }
}
