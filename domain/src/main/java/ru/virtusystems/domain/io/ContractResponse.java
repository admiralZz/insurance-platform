package ru.virtusystems.domain.io;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.Contract;

@Value
@Builder
public class ContractResponse {
    Contract contract;
}


