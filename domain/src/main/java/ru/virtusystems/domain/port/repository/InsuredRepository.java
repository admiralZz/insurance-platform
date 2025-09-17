package ru.virtusystems.domain.port.repository;

import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.domain.model.InsuredRequiredParams;

import java.util.Optional;

public interface InsuredRepository {
    Optional<Insured> findByRequiredParams(InsuredRequiredParams requiredParams);

    Insured save(Insured existing);
}


