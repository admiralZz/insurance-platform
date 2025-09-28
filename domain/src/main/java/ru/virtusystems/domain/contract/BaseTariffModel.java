package ru.virtusystems.domain.contract;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.virtusystems.domain.model.evaluator.TariffModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}
