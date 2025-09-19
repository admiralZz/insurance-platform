package ru.virtusystems.platform.database.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.port.repository.ProductRepository;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.model.ProductEntity;
import ru.virtusystems.platform.database.repository.ContractEntityRepository;
import ru.virtusystems.platform.database.repository.ProductEntityRepository;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.mapper.ProductMapper;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryJpaAdapter implements ProductRepository {

    private final ProductEntityRepository productEntityRepository;
    private final ProductMapper productMapper;

    @Override
    public Optional<Product> findByName(String name) {
        return productEntityRepository.findByName(name)
                .map(productMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity save = productEntityRepository.save(productMapper.toEntity(product));
        return productMapper.toDomain(save);
    }
}
