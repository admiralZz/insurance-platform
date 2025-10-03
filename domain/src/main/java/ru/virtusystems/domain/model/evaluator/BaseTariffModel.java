package ru.virtusystems.domain.model.evaluator;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.virtusystems.domain.model.types.ContractParameter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@SuperBuilder
public class BaseTariffModel extends TariffModel {
    public BigDecimal getTotalPremium() {
        return BigDecimal.ZERO;
    }
    public BigDecimal getInsuranceSum() {
        return BigDecimal.ZERO;
    }
    public LocalDateTime getEndDate() {
        return LocalDateTime.now();
    }
    public String getProductNumberCode() {
        return "";
    }

    @Override
    public List<ContractParameter> buildParametersState() {
        return List.of();
    }


    protected Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime
                .atZone(ZoneId.systemDefault())  // привязываем к часовому поясу
                .toInstant());
    }
}
