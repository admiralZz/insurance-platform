package ru.virtusystems.platform.service.office;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.domain.product.ProductFacade;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.model.ProductEntity;
import ru.virtusystems.platform.database.repository.ContractEntityRepository;
import ru.virtusystems.platform.dto.AccessibleTypeDto;
import ru.virtusystems.platform.dto.ReadAccessibleTypesDto;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.mapper.ContractMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficeContractService {
    private final ContractEntityRepository contractEntityRepository;
    private final ContractMapper contractMapper;
    private final Map<String, ProductFacade> productServices;

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
        ProductFacade productFacade = getProductFacade(contractEntity);
        return ReadAccessibleTypesDto.builder()
                .accessibleTypes(productFacade.getAccessibleTypesCollector().getAccessibleTypesMap()
                        .entrySet()
                        .stream()
                        .map(entry -> AccessibleTypeDto.builder()
                                .parameterCode(entry.getKey())
                                .valuesAndCodesMap(entry.getValue())
                                .build())
                        .toList())
                .build();
    }


    // TODO вынести в отдельный коллектор продуктов
    private ProductFacade getProductFacade(ContractEntity contractEntity) {
        String productName = Optional.of(contractEntity.getProduct())
                .map(ProductEntity::getName)
                .orElseThrow(() -> new IllegalStateException("Не удалось определить продукт"));
        // TODO проверка если вернет нул
        return productServices.get(productName);
    }

}
