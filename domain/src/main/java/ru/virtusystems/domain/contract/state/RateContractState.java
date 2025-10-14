package ru.virtusystems.domain.contract.state;

import ru.virtusystems.domain.model.types.ContractStatus;

import java.util.Set;

public class RateContractState extends ContractState {

    public RateContractState(ContractStateContext context) {
        super(context);
    }

    @Override
    public ContractStatus getStatus() {
        return ContractStatus.RATE;
    }

    @Override
    public Set<ContractStatus> allowedTransitions() {
        return Set.of(ContractStatus.PROJECT, ContractStatus.ISSUED);
    }

    @Override
    public void toRateState() {
        // nothing
    }

    @Override
    public void toProjectState() {
        context.changeState(new ProjectContractState(context));
    }

    @Override
    public void toIssuedState() {
        context.changeState(new IssuedContractState(context));
    }

    @Override
    public void toAnnulState() {
        throw new IllegalStateException("Договор не оформлен. Аннулирование невозможно");
    }
}
