package ru.virtusystems.platform.database.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.virtusystems.database.model.ContractNumberCounter;

import java.util.Optional;

public interface ContractNumberCounterRepository extends JpaRepository<ContractNumberCounter, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // ставим блокировку на строку
    @Query("select c from ContractNumberCounter c where c.id = :id")
    Optional<ContractNumberCounter> findByIdForUpdate(@Param("id") Long id);
}


