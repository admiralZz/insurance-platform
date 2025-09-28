package ru.virtusystems.domain.generators;

import ru.virtusystems.domain.model.Product;

public interface ContractNumberGenerator {
    String generateContractNumber(String productPrefixCode, Product product);
}
