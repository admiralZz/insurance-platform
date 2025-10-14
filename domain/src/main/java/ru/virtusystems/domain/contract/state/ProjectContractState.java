package ru.virtusystems.domain.contract.state;

import ru.virtusystems.domain.model.types.ContractStatus;

import java.util.Set;

public class ProjectContractState extends ContractState {

    public ProjectContractState(ContractStateContext context) {
        super(context);
    }

    @Override
    public ContractStatus getStatus() {
        return ContractStatus.PROJECT;
    }

    @Override
    public Set<ContractStatus> allowedTransitions() {
        return Set.of(ContractStatus.RATE, ContractStatus.ISSUED);
    }

    @Override
    public void toRateState() {
        context.changeState(new RateContractState(context));
    }

    @Override
    public void toProjectState() {
        // nothing
    }

    @Override
    public  void toIssuedState() {
        context.changeState(new IssuedContractState(context));
    }

    @Override
    public void toAnnulState() {
        throw new IllegalStateException("Договор не оформлен. Аннулирование невозможно");
    }
}
