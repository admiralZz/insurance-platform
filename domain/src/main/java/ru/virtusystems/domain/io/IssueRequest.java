package ru.virtusystems.domain.io;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IssueRequest {
    Long policyId;
}
