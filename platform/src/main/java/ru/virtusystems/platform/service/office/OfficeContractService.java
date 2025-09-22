package ru.virtusystems.platform.service.office;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.repository.ContractEntityRepository;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.mapper.ContractMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficeContractService {
    private final ContractEntityRepository contractEntityRepository;
    private final ContractMapper contractMapper;

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
}
