package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.ContractNumberCounter;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.repository.ContractNumberCounterRepository;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;
import ru.virtusystems.domain.product.zachitadohoda20.model.ZachitaDohoda20TariffModel;

@RequiredArgsConstructor
public class ZachitaDohoda20ContractNumberGenerator {

    private final ContractNumberCounterRepository contractNumberCounterRepository;

    public String generateContractNumber(ZachitaDohoda20TariffModel newState, Product product) {
        ContractNumberCounter counter = contractNumberCounterRepository.findById(product.getId())
                .orElseGet(() -> {
                    ContractNumberCounter c = new ContractNumberCounter();
                    c.setId(product.getId());
                    c.setCounter(0L);
                    c.setProduct(product);
                    return c;
                });

        counter.setCounter(counter.getCounter() + 1);
        contractNumberCounterRepository.save(counter);

        return newState.getProductNumberCode() + String.format("%07d", counter.getCounter());
    }
}
