package ru.virtusystems.domain.validation;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.domain.port.validation.CalcValidateService;
import ru.virtusystems.domain.port.validation.IssueValidateService;
import ru.virtusystems.domain.port.validation.ValidatedRequest;
import ru.virtusystems.domain.validation.exception.IssueValidationException;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class StandardIssueValidateService implements IssueValidateService {

    @Override
    public void validate(Contract contract) throws IssueValidationException {
        Optional.ofNullable(contract.getCalcDate()) // TODO надо проверить что точно должно быть датой заключения
                .orElseThrow(() -> new IssueValidationException("Не указана дата заключения"));
        Optional.ofNullable(contract.getStartDate())
                .orElseThrow(() -> new IssueValidationException("Не указана дата начала действия договора"));
        Optional.ofNullable(contract.getEndDate())
                .orElseThrow(() -> new IssueValidationException("Не указана дата окончания действия договора"));

        Insured insured = Optional.ofNullable(contract.getInsured())
                .orElseThrow(() -> new IssueValidationException("Не указан страхователь"));
        if (isNullOrEmpty(insured.getFirstName())) {
            throw new IssueValidationException("Не указано имя");
        }
        if (isNullOrEmpty(insured.getLastName())) {
            throw new IssueValidationException("Не указана фамилия");
        };
        if (insured.getBirthDate() == null) {
            throw new IssueValidationException("Не указано дата рождения");
        }
        if (isNullOrEmpty(insured.getDocSeries())) {
            throw new IssueValidationException("Не указана серия документа");
        }
        if (isNullOrEmpty(insured.getDocNumber())) {
            throw new IssueValidationException("Не указан номер документа");
        }

    }

    private boolean isNullOrEmpty(String string) {
        return Optional.ofNullable(string)
                .map(val -> val.isEmpty() && val.isBlank())
                .isEmpty();
    }
}
