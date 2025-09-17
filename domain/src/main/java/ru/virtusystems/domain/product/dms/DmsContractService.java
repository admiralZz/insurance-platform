package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.types.ContractStatus;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DmsContractService {
    private final DmsCalculationService dmsCalculationService;
    private final ClientService clientService;
    private final DmsCalcIdGenerator dmsCalcIdGenerator;
    private final DmsContractNumberGenerator dmsContractNumberGenerator;
    private final DmsContractDatesService contractDatesService;

    private final ContractRepository contractRepository;

    public Contract calculate(DmsCalculateRequest calculateRequest) {
        DmsTariffModel newState = dmsCalculationService.calculateTariffModel(calculateRequest);

        return contractRepository.save(Contract.builder()
                .calcId(dmsCalcIdGenerator.generateCalcId())
                .params(newState.getParameters())
                .premium(newState.getTotalPremium())
                .insuredSum(newState.getInsuranceSum())
                .calcDate(LocalDateTime.now())
                .startDate(contractDatesService.startDate())
                .endDate(newState.getEndDate())
                // TODO можно сделать StateMachine для контроля переходов между статусами
                .status(ContractStatus.RATE)
                .build());

    }

    public Contract save(SaveRequest<DmsCalculateRequest> saveRequest) {
        DmsCalculateRequest calculateRequest = saveRequest.getCalcRequest();
        DmsTariffModel newState;
        if (calculateRequest != null) {
            newState = dmsCalculationService.calculateTariffModel(calculateRequest);
        } else {
            newState = DmsTariffModel.builder().build();
        }

        return contractRepository.save(Contract.builder().calcId(dmsCalcIdGenerator.generateCalcId())
                .number(dmsContractNumberGenerator.generateContractNumber(newState))
                .params(newState.getParameters())
                .premium(newState.getTotalPremium())
                .insuredSum(newState.getInsuranceSum())
                .calcDate(LocalDateTime.now())
                .startDate(contractDatesService.startDate())
                .endDate(newState.getEndDate())
                .insured(clientService.updateOrCreateInsured(saveRequest.getInsured()))
                // TODO можно сделать StateMachine для контроля переходов между статусами
                .status(ContractStatus.PROJECT)
                .build());
    }

    public Contract update(UpdateRequest<DmsCalculateRequest> updateRequest) {
        return contractRepository.findById(updateRequest.getPolicyId())
                .map(contract -> {
                    DmsCalculateRequest calculateRequest = updateRequest.getCalcRequest();
                    DmsTariffModel newState;
                    if (calculateRequest != null) {
                        newState = dmsCalculationService.calculateTariffModel(calculateRequest);
                        contract.setCalcId(dmsCalcIdGenerator.generateCalcId());
                        contract.setPremium(newState.getTotalPremium());
                        contract.setInsuredSum(newState.getInsuranceSum());
                        contract.setCalcDate(LocalDateTime.now());
                        contract.setStartDate(contractDatesService.startDate());
                        contract.setEndDate(newState.getEndDate());
                    }

                    contract.setInsured(clientService.updateOrCreateInsured(updateRequest.getInsured()));
                    // TODO можно сделать StateMachine для контроля переходов между статусами
                    contract.setStatus(ContractStatus.PROJECT);

                    return contractRepository.save(contract);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Contract with id=" + updateRequest.getPolicyId() + " not found"
                ));
    }

    public Contract issue(IssueRequest issueRequest) {
        return contractRepository.findById(issueRequest.getPolicyId())
                .map(contract -> {
                    // TODO проверки перед оформлением(премия != null, всякие обязательные штуки для оформления и т.д.)
                    contract.setStatus(ContractStatus.ISSUED);

                    return contractRepository.save(contract);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Contract with id=" + issueRequest.getPolicyId() + " not found"
                ));

    }
}
