package ru.virtusystems.platform.api.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IssueRequest {
    Long policyId;
}
