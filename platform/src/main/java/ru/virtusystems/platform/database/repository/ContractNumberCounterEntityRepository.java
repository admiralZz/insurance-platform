package ru.virtusystems.platform.database.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.virtusystems.platform.database.model.ContractNumberCounterEntity;

import java.util.Optional;

public interface ContractNumberCounterEntityRepository extends JpaRepository<ContractNumberCounterEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // ставим блокировку на строку
    @Query("select c from ContractNumberCounterEntity c where c.id = :id")
    Optional<ContractNumberCounterEntity> findByIdForUpdate(@Param("id") Long id);
}


