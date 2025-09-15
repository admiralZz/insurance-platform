package ru.virtusystems.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.database.model.Insured;
import ru.virtusystems.database.repository.InsuredRepository;
import ru.virtusystems.dto.CreateInsuredDto;
import ru.virtusystems.dto.InsuredRequiredParams;
import ru.virtusystems.mapper.InsuredMapper;
import ru.virtusystems.service.port.ClientService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandardClientService implements ClientService {
    private final InsuredRepository insuredRepository;
    private final InsuredMapper insuredMapper;

    @Override
    public Optional<Insured> findInsuredByRequiredParams(InsuredRequiredParams insuredRequiredParams) {
        return insuredRepository.findInsuredByFirstNameAndLastNameAndBirthDateAndDocSeriesAndDocNumber(
                insuredRequiredParams.getFirstName(),
                insuredRequiredParams.getLastName(),
                insuredRequiredParams.getBirthDate(),
                insuredRequiredParams.getPassportSerial(),
                insuredRequiredParams.getPassportNumber()
        );
    }

    /* TODO возможные проблемы
    1. Дубликаты при конкурентных вставках
    Если два потока одновременно не найдут insured и оба дойдут до save(), то могут появиться 2 одинаковые записи.
    👉 лучше добавить уникальный индекс в БД на (firstName, lastName, birthDate, docSeries, docNumber) и обработать исключение (DataIntegrityViolationException).

    2. Lost update
    Если два потока читают одну запись и одновременно обновляют разные поля, без @Version одно обновление может затереть другое.
    👉 решение: добавить @Version для оптимистической блокировки.

    3. Маппинг DTO → Entity
    Нужно быть аккуратным, чтобы insuredMapper.updateEntityFromDto не затирал не переданные поля null-ами.

     */
    @Transactional
    public Insured updateOrCreateInsured(CreateInsuredDto createInsuredDto) {
        // Если застраховынный уже существует, то обновляем его данные из запроса
        Insured newOrUpdated = Optional.ofNullable(createInsuredDto)
                .flatMap(dto -> findInsuredByRequiredParams(InsuredRequiredParams.builder()
                                .firstName(dto.getFirstName())
                                .lastName(dto.getLastName())
                                .birthDate(dto.getBirthDate())
                                .passportSerial(dto.getDocSeries())
                                .passportNumber(dto.getDocNumber())
                                .build())
                        .map(insured -> {
                            insuredMapper.updateEntityFromDto(dto, insured);
                            return insured;
                        }))
                .orElseGet(() -> insuredMapper.toEntity(createInsuredDto));

        return insuredRepository.save(newOrUpdated);
    }
}
