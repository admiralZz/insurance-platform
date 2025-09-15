package ru.virtusystems.platform.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.virtusystems.database.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {}


