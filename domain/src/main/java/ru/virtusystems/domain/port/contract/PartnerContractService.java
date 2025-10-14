package ru.virtusystems.domain.port.contract;

import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;

public interface PartnerContractService {
    Contract calculate(CalculateRequest calculateRequest);
    Contract save(SaveRequest saveRequest);
    Contract update(UpdateRequest updateRequest);
    Contract issue(IssueRequest issueRequest) throws Exception;
}
