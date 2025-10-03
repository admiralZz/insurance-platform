package ru.virtusystems.domain.product;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.port.repository.ProductRepository;

@RequiredArgsConstructor
public class StandardProductService implements ProductService {

    private final ProductRepository productRepository;

    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product getOrCreate(Product product) {
        return productRepository.findByName(product.getName())
                .orElseGet(() -> productRepository.save(product));
    }

}
