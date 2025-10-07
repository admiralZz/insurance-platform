package ru.virtusystems.domain.port.repository;


import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Product;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface ContractRepository {

    Contract save(Contract contract);

    Optional<Contract> findByIdAndProduct(Long id, Product product);
    Optional<Contract> findByCalcIdAndProduct(String calcId, Product product);
}


