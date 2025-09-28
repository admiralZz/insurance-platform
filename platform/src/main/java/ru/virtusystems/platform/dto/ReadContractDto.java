package ru.virtusystems.platform.dto;

import lombok.Builder;
import lombok.Value;
import ru.virtusystems.domain.model.types.ContractParameter;
import ru.virtusystems.domain.model.types.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ReadContractDto {
    Long id;
    ReadProductDto product;
    ReadInsuredDto insured;
    ContractStatus status;
    String calcId;
    String number;
    LocalDateTime calcDate;
    LocalDateTime issueDate;
    LocalDateTime startDate;
    LocalDateTime endDate;
    BigDecimal premium;
    BigDecimal insuredSum;
    List<ContractParameter> params;

}
