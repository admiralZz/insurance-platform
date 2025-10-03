package ru.virtusystems.domain.port.generator;

import ru.virtusystems.domain.model.Product;

public interface CalcGenerator {
    String generateCalcId(Product product);
}
