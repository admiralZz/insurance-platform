package ru.virtusystems.domain.port.product;

import ru.virtusystems.domain.model.Product;

public interface ProductService {
    Product getProductByName(String name);
    Product getOrCreate(Product product);
}
