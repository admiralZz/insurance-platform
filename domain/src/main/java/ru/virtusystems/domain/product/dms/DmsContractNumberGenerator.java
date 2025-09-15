package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.database.model.ContractNumberCounter;
import ru.virtusystems.database.repository.ContractNumberCounterRepository;
import ru.virtusystems.dto.DmsTariffModel;

@Service
@RequiredArgsConstructor
public class DmsContractNumberGenerator {

    private static final Long DMS_ID = 1L;
    private final ContractNumberCounterRepository contractNumberCounterRepository;

    @Transactional
    public String generateContractNumber(DmsTariffModel newState) {
        ContractNumberCounter counter = contractNumberCounterRepository.findByIdForUpdate(DMS_ID)
                .orElseGet(() -> {
                    ContractNumberCounter c = new ContractNumberCounter();
                    c.setId(DMS_ID);
                    c.setCounter(0L);
                    return c;
                });

        counter.setCounter(counter.getCounter() + 1);
        contractNumberCounterRepository.save(counter);

        return newState.getProductNumberCode() + String.format("%07d", counter.getCounter());
    }
}
