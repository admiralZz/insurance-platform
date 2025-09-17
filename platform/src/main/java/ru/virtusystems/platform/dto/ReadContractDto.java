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
    String calcId;
    String number;
    ContractStatus status;
    LocalDateTime calcDate;
    LocalDateTime issueDate;
    LocalDateTime startDate;
    LocalDateTime endDate;
    BigDecimal premium;
    BigDecimal insuredSum;
    ReadInsuredDto insured;
    List<ContractParameter> params;

}
