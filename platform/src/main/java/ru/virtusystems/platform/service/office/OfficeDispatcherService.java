package ru.virtusystems.platform.service.office;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.port.product.ProductFacade;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.model.ProductEntity;
import ru.virtusystems.platform.database.repository.ContractEntityRepository;
import ru.virtusystems.platform.dto.AccessibleTypeDto;
import ru.virtusystems.platform.dto.ReadAccessibleTypesDto;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.service.ProductCollector;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficeDispatcherService {
    private final ContractEntityRepository contractEntityRepository;
    private final ContractMapper contractMapper;
    private final ProductCollector productCollector;

    public List<ReadContractDto> getAllContracts() {
        return contractEntityRepository.findAll()
                .stream()
                .map(contractMapper::toDto)
                .toList();
    }

    public ReadContractDto getContractById(Long contractId) {
        return contractEntityRepository.findById(contractId)
                .map(contractMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Договор id = " + contractId + " не найден"));
    }

    public ReadAccessibleTypesDto getAccessibleTypesById(Long contractId) {
        ContractEntity contractEntity = contractEntityRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Договор id = " + contractId + " не найден"));
        String productName = Optional.ofNullable(contractEntity.getProduct())
                .map(ProductEntity::getName)
                .orElseThrow(() -> new IllegalStateException("Не удалось определить продукт договора id = " + contractId));
        ProductFacade productFacade = productCollector.getProductFacadeByName(productName);
        return ReadAccessibleTypesDto.builder()
                .accessibleTypes(productFacade.getOfficeContractService().getAccessibleTypesMap()
                        .entrySet()
                        .stream()
                        .map(entry -> AccessibleTypeDto.builder()
                                .parameterCode(entry.getKey())
                                .valuesAndCodesMap(entry.getValue())
                                .build())
                        .toList())
                .build();
    }

    public ReadAccessibleTypesDto getAccessibleTypesByProduct(String productName) {
        ProductFacade productFacade = productCollector.getProductFacadeByName(productName);
        return ReadAccessibleTypesDto.builder()
                .accessibleTypes(productFacade.getOfficeContractService().getAccessibleTypesMap()
                        .entrySet()
                        .stream()
                        .map(entry -> AccessibleTypeDto.builder()
                                .parameterCode(entry.getKey())
                                .valuesAndCodesMap(entry.getValue())
                                .build())
                        .toList())
                .build();
    }

}
