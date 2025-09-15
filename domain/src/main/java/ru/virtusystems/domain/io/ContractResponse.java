package ru.virtusystems.domain.io;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.dto.ReadContractDto;

@Value
@Builder
public class ContractResponse {
    ReadContractDto contract;
}


