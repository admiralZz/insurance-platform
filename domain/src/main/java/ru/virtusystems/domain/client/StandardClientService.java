package ru.virtusystems.domain.client;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.virtusystems.domain.mapper.InsuredMapper;
import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.domain.model.InsuredRequiredParams;
import ru.virtusystems.domain.port.repository.InsuredRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class StandardClientService implements ClientService {
    private final InsuredRepository insuredRepository;
    private final InsuredMapper insuredMapper = Mappers.getMapper(InsuredMapper.class);

    @Override
    public Optional<Insured> findInsuredByRequiredParams(InsuredRequiredParams insuredRequiredParams) {
        return insuredRepository.findByRequiredParams(insuredRequiredParams);
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
    @Override
    public Insured updateOrCreateInsured(Insured newInsured) {
        InsuredRequiredParams params = InsuredRequiredParams.builder()
                .firstName(newInsured.getFirstName())
                .lastName(newInsured.getLastName())
                .birthDate(newInsured.getBirthDate())
                .passportSerial(newInsured.getDocSeries())
                .passportNumber(newInsured.getDocNumber())
                .build();

        // Если застраховынный уже существует, то обновляем его данные из запроса
        return insuredRepository.findByRequiredParams(params)
                .map(existing -> {
                    insuredMapper.updateInsured(newInsured, existing);
                    return insuredRepository.save(existing);
                })
                .orElseGet(() -> insuredRepository.save(newInsured));
    }
}
