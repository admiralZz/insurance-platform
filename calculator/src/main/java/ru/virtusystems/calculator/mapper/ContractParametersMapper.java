package ru.virtusystems.calculator.mapper;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.calculator.IOParameter;
import ru.virtusystems.domain.model.types.ContractParameter;
import ru.virtusystems.domain.port.AccessibleTypesCollector;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ContractParametersMapper {

    private final AccessibleTypesCollector accessibleTypesCollector;

    public List<IOParameter> mapToInputParameters(List<ContractParameter> contractParameters) {
        return contractParameters.stream()
                .map(contractParameter -> new IOParameter(
                                contractParameter.getName(),
                                contractParameter.getCode(),
                                accessibleTypesCollector.getAccessibleType(contractParameter),
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
}
