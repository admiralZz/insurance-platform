package ru.virtusystems.domain.port.repository;

import ru.virtusystems.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByName(String name);
    Product save(Product product);
}
