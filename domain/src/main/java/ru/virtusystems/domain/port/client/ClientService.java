package ru.virtusystems.domain.port.client;


import ru.virtusystems.domain.model.Insured;
import ru.virtusystems.domain.model.InsuredRequiredParams;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Insured> getClientList();
    Optional<Insured> findInsuredByRequiredParams(InsuredRequiredParams insuredRequiredParams);
    Insured updateOrCreateInsured(Insured newInsured);
}
