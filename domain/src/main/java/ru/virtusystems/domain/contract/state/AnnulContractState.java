package ru.virtusystems.domain.contract.state;

import ru.virtusystems.domain.model.types.ContractStatus;

import java.util.Set;

public class AnnulContractState extends ContractState {

    public AnnulContractState(ContractStateContext context) {
        super(context);
    }

    @Override
    public ContractStatus getStatus() {
        return ContractStatus.ANNUL;
    }

    @Override
    public Set<ContractStatus> allowedTransitions() {
        return Set.of();
    }

    @Override
    public void toRateState() {
        throw new IllegalStateException("Договор уже аннулирован");
    }

    @Override
    public void toProjectState() {
        throw new IllegalStateException("Договор уже аннулирован");
    }

    @Override
    public void toIssuedState() {
        throw new IllegalStateException("Договор уже аннулирован");
    }

    @Override
    public void toAnnulState() {
        // nothing
    }
}
