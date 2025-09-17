package ru.virtusystems.domain.port.repository;


import ru.virtusystems.domain.model.Contract;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface ContractRepository {

    Contract save(Contract contract);

    Optional<Contract> findById(Long id);
}


