package ru.virtusystems.domain.product.dms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.api.request.DmsCalculateRequest;
import ru.virtusystems.api.request.IssueRequest;
import ru.virtusystems.api.request.SaveRequest;
import ru.virtusystems.api.request.UpdateRequest;
import ru.virtusystems.api.response.ContractResponse;
import ru.virtusystems.database.model.Contract;
import ru.virtusystems.database.model.types.ContractStatus;
import ru.virtusystems.database.repository.ContractRepository;
import ru.virtusystems.dto.DmsTariffModel;
import ru.virtusystems.mapper.ContractMapper;
import ru.virtusystems.service.StandardClientService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DmsContractService {
    private final DmsCalculationService dmsCalculationService;
    private final StandardClientService insuredService;
    private final DmsCalcIdGenerator dmsCalcIdGenerator;
    private final DmsContractNumberGenerator dmsContractNumberGenerator;
    private final DmsContractDatesService contractDatesService;

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    @Transactional
    public ContractResponse calculate(DmsCalculateRequest calculateRequest) {
        DmsTariffModel newState = dmsCalculationService.calculateTariffModel(calculateRequest);

        Contract.ContractBuilder contractBuilder = Contract.builder();
        contractBuilder.calcId(dmsCalcIdGenerator.generateCalcId())
                .params(newState.getParameters())
                .premium(newState.getTotalPremium())
                .insuredSum(newState.getInsuranceSum())
                .calcDate(LocalDateTime.now())
                .startDate(contractDatesService.startDate())
                .endDate(newState.getEndDate());

        contractBuilder.status(ContractStatus.RATE);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contractRepository.save(contractBuilder.build())))
                .build();
    }

    @Transactional
    public ContractResponse save(SaveRequest saveRequest) {
        DmsCalculateRequest calculateRequest = saveRequest.getCalcRequest();
        DmsTariffModel newState;
        if (calculateRequest != null) {
            newState = dmsCalculationService.calculateTariffModel(calculateRequest);
        } else {
            newState = DmsTariffModel.builder().build();
        }


        Contract.ContractBuilder contractBuilder = Contract.builder();
        contractBuilder.calcId(dmsCalcIdGenerator.generateCalcId())
                .number(dmsContractNumberGenerator.generateContractNumber(newState))
                .params(newState.getParameters())
                .premium(newState.getTotalPremium())
                .insuredSum(newState.getInsuranceSum())
                .calcDate(LocalDateTime.now())
                .startDate(contractDatesService.startDate())
                .endDate(newState.getEndDate());

        contractBuilder.insured(insuredService.updateOrCreateInsured(saveRequest.getInsured()));
        // TODO можно сделать StateMachine для контроля переходов между статусами
        contractBuilder.status(ContractStatus.PROJECT);

        return ContractResponse.builder()
                .contract(contractMapper.toDto(contractRepository.save(contractBuilder.build())))
                .build();
    }

    @Transactional
    public ContractResponse update(UpdateRequest updateRequest) {
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

                    contract.setInsured(insuredService.updateOrCreateInsured(updateRequest.getInsured()));
                    // TODO можно сделать StateMachine для контроля переходов между статусами
                    contract.setStatus(ContractStatus.PROJECT);

                    return ContractResponse.builder()
                            .contract(contractMapper.toDto(contractRepository.save(contract)))
                            .build();
                })
                .orElseThrow(() -> new RuntimeException(
                        "Contract with id=" + updateRequest.getPolicyId() + " not found"
                ));
    }

    @Transactional
    public ContractResponse issue(IssueRequest issueRequest) {
        return contractRepository.findById(issueRequest.getPolicyId())
                .map(contract -> {
                    // TODO проверки перед оформлением(премия != null, всякие обязательные штуки для оформления и т.д.)
                    contract.setStatus(ContractStatus.ISSUED);
                    Contract save = contractRepository.save(contract);

                    return ContractResponse.builder()
                            .contract(contractMapper.toDto(save))
                            .build();
                })
                .orElseThrow(() -> new RuntimeException(
                        "Contract with id=" + issueRequest.getPolicyId() + " not found"
                ));

    }
}
