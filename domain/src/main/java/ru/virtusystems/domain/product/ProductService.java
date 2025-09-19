package ru.virtusystems.domain.product;

import ru.virtusystems.domain.model.Product;

public interface ProductService {
    Product getProductByName(String name);
    Product getOrCreate(Product product);
}
