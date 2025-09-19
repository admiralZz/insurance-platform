package ru.virtusystems.domain.product;

import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Product;

public interface ContractService {
    // TODO Убрать это отсюда
    Product create();
    Contract calculate(CalculateRequest calculateRequest);
    Contract save(SaveRequest saveRequest);
    Contract update(UpdateRequest updateRequest);
    Contract issue(IssueRequest issueRequest);
}
