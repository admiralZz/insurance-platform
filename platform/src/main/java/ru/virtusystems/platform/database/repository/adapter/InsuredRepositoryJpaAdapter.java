package ru.virtusystems.platform.database.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.domain.model.InsuredRequiredParams;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.port.repository.InsuredRepository;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.model.InsuredEntity;
import ru.virtusystems.platform.database.repository.ContractEntityRepository;
import ru.virtusystems.platform.database.repository.InsuredEntityRepository;
import ru.virtusystems.platform.mapper.ContractMapper;
import ru.virtusystems.platform.mapper.InsuredMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InsuredRepositoryJpaAdapter implements InsuredRepository {

    private final InsuredEntityRepository insuredEntityRepository;
    private final InsuredMapper insuredMapper;

    @Override
    public Optional<Insured> findByRequiredParams(InsuredRequiredParams requiredParams) {
        return insuredEntityRepository.findInsuredByFirstNameAndLastNameAndBirthDateAndDocSeriesAndDocNumber(
                        requiredParams.getFirstName(),
                        requiredParams.getLastName(),
                        requiredParams.getBirthDate(),
                        requiredParams.getPassportSerial(),
                        requiredParams.getPassportNumber()
                )
                .map(insuredMapper::toDomain);
    }

    @Override
    public Insured save(Insured insured) {
        InsuredEntity entity = insuredMapper.toEntity(insured);
        InsuredEntity saved = insuredEntityRepository.save(entity);
        return insuredMapper.toDomain(saved);
    }

    @Override
    public List<Insured> findAll() {
        return insuredEntityRepository.findAll().stream()
                .map(insuredMapper::toDomain)
                .toList();
    }
}
