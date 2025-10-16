package ru.virtusystems.platform.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.model.ProductEntity;

import java.util.Optional;

public interface ContractEntityRepository extends JpaRepository<ContractEntity, Long> {
    Optional<ContractEntity> findByIdAndProduct(Long id, ProductEntity product);
    Optional<ContractEntity> findByCalcIdAndProduct(String calcId, ProductEntity product);
    Optional<ContractEntity> findByNumber(String number);
    Page<ContractEntity> findAllBy(Pageable pageable);
}


