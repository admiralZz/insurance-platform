package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.ContractNumberCounter;
import ru.virtusystems.domain.port.repository.ContractNumberCounterRepository;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;

@RequiredArgsConstructor
public class DmsContractNumberGenerator {

    private static final Long DMS_ID = 1L;
    private final ContractNumberCounterRepository contractNumberCounterRepository;

    public String generateContractNumber(DmsTariffModel newState) {
        ContractNumberCounter counter = contractNumberCounterRepository.findById(DMS_ID)
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
