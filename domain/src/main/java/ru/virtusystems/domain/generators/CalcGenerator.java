package ru.virtusystems.domain.generators;

import ru.virtusystems.domain.model.Product;

public interface CalcGenerator {
    String generateCalcId(Product product);
}
