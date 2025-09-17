package ru.virtusystems.platform.api.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductIssueRequest extends ProductRequest {
    Long policyId;
}
