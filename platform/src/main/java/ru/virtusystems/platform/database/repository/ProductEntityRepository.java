package ru.virtusystems.platform.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.virtusystems.platform.database.model.ProductEntity;

import java.util.Optional;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
}
