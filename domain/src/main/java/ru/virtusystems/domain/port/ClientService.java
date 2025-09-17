package ru.virtusystems.domain.port;


import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.domain.model.InsuredRequiredParams;

import java.util.Optional;

public interface ClientService {
    Optional<Insured> findInsuredByRequiredParams(InsuredRequiredParams insuredRequiredParams);
    Insured updateOrCreateInsured(Insured newInsured);
}
