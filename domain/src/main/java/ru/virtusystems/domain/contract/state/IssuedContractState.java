package ru.virtusystems.domain.contract.state;

import ru.virtusystems.domain.model.types.ContractStatus;

import java.util.Set;

public class IssuedContractState extends ContractState {

    public IssuedContractState(ContractStateContext context) {
        super(context);
    }

    @Override
    public ContractStatus getStatus() {
        return ContractStatus.ISSUED;
    }

    @Override
    public Set<ContractStatus> allowedTransitions() {
        return Set.of(ContractStatus.ANNUL);
    }

    @Override
    public void toRateState() {
        throw new IllegalStateException("Договор уже оформлен");
    }

    @Override
    public void toProjectState() {
        throw new IllegalStateException("Договор уже оформлен");
    }

    @Override
    public void toIssuedState() {
        // nothing
    }

    @Override
    public void toAnnulState() {
        context.changeState(new AnnulContractState(context));
    }
}
