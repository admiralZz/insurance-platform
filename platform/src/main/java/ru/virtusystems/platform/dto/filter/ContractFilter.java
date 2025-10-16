package ru.virtusystems.platform.dto.filter;

import ru.virtusystems.domain.model.types.ContractStatus;

public record ContractFilter(ContractStatus status,
                             String product) {
}
