package ru.virtusystems.domain.port.generator;

import ru.virtusystems.domain.model.Product;

public interface ContractNumberGenerator {
    String generateContractNumber(String productPrefixCode, Product product);
}
