package ru.virtusystems.domain.generators;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.ContractNumberCounter;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.port.repository.ContractNumberCounterRepository;

@RequiredArgsConstructor
public class StandardContractNumberGenerator implements ContractNumberGenerator {

    private final ContractNumberCounterRepository contractNumberCounterRepository;

    @Override
    public String generateContractNumber(String productPrefixCode, Product product) {
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

        return productPrefixCode + String.format("%07d", counter.getCounter());
    }
}
