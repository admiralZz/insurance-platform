package ru.virtusystems.domain.contract.state;

import lombok.Getter;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.types.ContractStatus;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class ContractStateContext {
    private static final Map<ContractStatus, Function<ContractStateContext, ContractState>> STATE_FACTORY = Map.of(
            ContractStatus.RATE, RateContractState::new,
            ContractStatus.PROJECT, ProjectContractState::new,
            ContractStatus.ISSUED, IssuedContractState::new,
            ContractStatus.ANNUL, AnnulContractState::new
    );
    private final Contract contract;
    private ContractState state;

    private ContractStateContext(Contract contract) {
        this.contract = contract;
        this.state = createStateFromStatus(contract.getStatus());
    }

    public static ContractStateContext init(Contract contract) {
        return new ContractStateContext(contract);
    }

    void changeState(ContractState newState) {
        if (this.state != null && !this.state.equals(newState)) {
             if (!this.state.canTransitionTo(newState.getStatus())) {
                 throw new IllegalStateException(
                         "Нельзя перейти из состояния " + this.state.getStatus() + " в " + newState.getStatus()
                 );
             }
        }

        this.state = newState;
        this.contract.setStatus(newState.getStatus());
    }

    private ContractState createStateFromStatus(ContractStatus status) {
        return STATE_FACTORY
                .get(Objects.requireNonNullElse(status, ContractStatus.PROJECT))
                .apply(this);
    }
}
