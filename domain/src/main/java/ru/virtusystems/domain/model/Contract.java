package ru.virtusystems.domain.model;

import lombok.*;
import ru.virtusystems.domain.model.types.ContractParameter;
import ru.virtusystems.domain.model.types.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    private Long id;

    private String calcId;

    private String number;

    private LocalDateTime calcDate;
    private LocalDateTime issueDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BigDecimal premium;
    private BigDecimal insuredSum;

    private ContractStatus status;

    private List<ContractParameter> params;

    private Insured insured;
}
