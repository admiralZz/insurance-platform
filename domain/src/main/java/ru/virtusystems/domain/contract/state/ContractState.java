package ru.virtusystems.domain.contract.state;

import ru.virtusystems.domain.model.types.ContractStatus;

import java.util.Set;

public abstract class ContractState {

    protected ContractStateContext context;

    public ContractState(ContractStateContext context) {
        this.context = context;
    }

    public boolean canTransitionTo(ContractStatus status) {
        return allowedTransitions().contains(status);
    }

    public abstract ContractStatus getStatus();
    public abstract Set<ContractStatus> allowedTransitions();
    public abstract void toRateState();
    public abstract void toProjectState();
    public abstract void toIssuedState();
    public abstract void toAnnulState();
}
