package ru.virtusystems.domain.port;

import ru.virtusystems.database.model.Insured;
import ru.virtusystems.dto.InsuredRequiredParams;

import java.util.Optional;

public interface ClientService {
    Optional<Insured> findInsuredByRequiredParams(InsuredRequiredParams insuredRequiredParams);
}
