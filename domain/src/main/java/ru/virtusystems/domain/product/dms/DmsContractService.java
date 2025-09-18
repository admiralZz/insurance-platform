package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.ContractService;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.types.ContractStatus;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.domain.product.dms.mapper.DmsCalculateRequestMapper;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DmsContractService implements ContractService {
    private final DmsCalculationService dmsCalculationService;
    private final ClientService clientService;
    private final DmsCalcIdGenerator dmsCalcIdGenerator;
    private final DmsContractNumberGenerator dmsContractNumberGenerator;
    private final DmsContractDatesService contractDatesService;
    private final DmsCalculateRequestMapper calculateRequestMapper;
    private final ContractRepository contractRepository;

    @Override
    public Contract calculate(CalculateRequest calculateRequest) {
        DmsCalculateRequest dmsCalculateRequest = calculateRequestMapper.toDmsCalculateRequest(calculateRequest);
        DmsTariffModel newState = dmsCalculationService.calculateTariffModel(dmsCalculateRequest);

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

    @Override
    public Contract save(SaveRequest saveRequest) {
        CalculateRequest calculateRequest = saveRequest.getCalcRequest();
        DmsTariffModel newState;
        if (calculateRequest != null) {
            DmsCalculateRequest dmsCalculateRequest = calculateRequestMapper.toDmsCalculateRequest(calculateRequest);
            newState = dmsCalculationService.calculateTariffModel(dmsCalculateRequest);
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

    @Override
    public Contract update(UpdateRequest updateRequest) {
        return contractRepository.findById(updateRequest.getPolicyId())
                .map(contract -> {
                    CalculateRequest calculateRequest = updateRequest.getCalcRequest();
                    DmsTariffModel newState;
                    if (calculateRequest != null) {
                        DmsCalculateRequest dmsCalculateRequest = calculateRequestMapper.toDmsCalculateRequest(calculateRequest);
                        newState = dmsCalculationService.calculateTariffModel(dmsCalculateRequest);
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

    @Override
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
