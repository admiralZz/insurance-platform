package ru.virtusystems.platform.api.response;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.dto.ReadContractDto;

@Value
@Builder
public class ContractResponse {
    ReadContractDto contract;
}


