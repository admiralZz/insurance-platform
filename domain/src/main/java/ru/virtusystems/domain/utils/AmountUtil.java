package ru.virtusystems.domain.utils;

import java.math.BigDecimal;

public class AmountUtil {

    public static BigDecimal getAmountByObject(Object value) {
        BigDecimal bigDecimalValue;
        if (value instanceof Long) {
            bigDecimalValue = BigDecimal.valueOf((Long) value);
        } else if (value instanceof Double) {
            bigDecimalValue = BigDecimal.valueOf((Double) value);
        } else {
            throw new RuntimeException("Неопределенный тип для преобразования");
        }

        return bigDecimalValue;
    }
}
