package ru.virtusystems.domain.contract;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.contract.state.ContractStateContext;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.mapper.CalculateRequestMapper;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.model.evaluator.BaseTariffModel;
import ru.virtusystems.domain.port.calculation.CalculationService;
import ru.virtusystems.domain.port.client.ClientService;
import ru.virtusystems.domain.port.contract.PartnerContractService;
import ru.virtusystems.domain.port.dates.ContractDatesService;
import ru.virtusystems.domain.port.generator.CalcGenerator;
import ru.virtusystems.domain.port.generator.ContractNumberGenerator;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.port.validation.IssueValidateService;
import ru.virtusystems.domain.port.validation.ValidatedRequest;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class StandardPartnerContractService implements PartnerContractService {

    private final String productName;
    private final CalculationService calculationService;
    private final IssueValidateService issueValidateService;
    private final ClientService clientService;
    private final ProductService productService;
    private final CalcGenerator calcIdGenerator;
    private final ContractNumberGenerator contractNumberGenerator;
    private final ContractDatesService contractDatesService;
    private final CalculateRequestMapper calculateRequestMapper;
    private final ContractRepository contractRepository;

    @Override
    public Contract calculate(CalculateRequest calculateRequest) {
        ValidatedRequest validatedRequest = calculateRequestMapper.map(calculateRequest);
        Product product = productService.getProductByName(productName);
        Optional<Contract> maybeContract = Optional.ofNullable(calculateRequest.getCalcId())
                .flatMap(calcId -> contractRepository.findByCalcIdAndProduct(calcId, product));

        if (maybeContract.isPresent()) {
            Contract contract = maybeContract.get();
            // Смена статуса должна происходить через данный контекст(state machine)
            ContractStateContext.init(contract)
                    .getState()
                    .toRateState();
            BaseTariffModel newState = evaluateState(validatedRequest);

            contract.setParams(newState.getParameters());
            contract.setPremium(newState.getTotalPremium());
            contract.setInsuredSum(newState.getInsuranceSum());
            contract.setCalcDate(LocalDateTime.now());
            contract.setStartDate(contractDatesService.startDate());
            contract.setEndDate(newState.getEndDate());

            return contractRepository.save(contract);
        }
        BaseTariffModel newState = evaluateState(validatedRequest);
        Contract newContract = Contract.builder()
                .product(product)
                .calcId(calcIdGenerator.generateCalcId(product))
                .params(newState.getParameters())
                .premium(newState.getTotalPremium())
                .insuredSum(newState.getInsuranceSum())
                .calcDate(LocalDateTime.now())
                .startDate(contractDatesService.startDate())
                .endDate(newState.getEndDate())
                .build();
        ContractStateContext.init(newContract)
                .getState()
                .toRateState();

        return contractRepository.save(newContract);

    }

    @Override
    public Contract save(SaveRequest saveRequest) {
        CalculateRequest calculateRequest = saveRequest.getCalcRequest();
        BaseTariffModel newState;
        if (calculateRequest != null) {
            ValidatedRequest validatedRequest = calculateRequestMapper.map(calculateRequest);
            newState = evaluateState(validatedRequest);
        } else {
            newState = BaseTariffModel.builder().build();
        }
        Product product = productService.getProductByName(productName);
        Contract newContract = Contract.builder()
                .product(product)
                .calcId(calcIdGenerator.generateCalcId(productService.getProductByName(productName)))
                .number(contractNumberGenerator.generateContractNumber(newState.getProductNumberCode(), product))
                .params(newState.getParameters())
                .premium(newState.getTotalPremium())
                .insuredSum(newState.getInsuranceSum())
                .calcDate(LocalDateTime.now())
                .startDate(contractDatesService.startDate())
                .endDate(newState.getEndDate())
                .insured(clientService.updateOrCreateInsured(saveRequest.getInsured()))
                .build();
        ContractStateContext.init(newContract)
                .getState()
                .toProjectState();

        return contractRepository.save(newContract);
    }

    @Override
    public Contract update(UpdateRequest updateRequest) {
        Product product = productService.getProductByName(productName);

        return contractRepository.findByIdAndProduct(updateRequest.getPolicyId(), product)
                .map(contract -> {
                    ContractStateContext.init(contract)
                            .getState()
                            .toProjectState();
                    CalculateRequest calculateRequest = updateRequest.getCalcRequest();
                    BaseTariffModel newState;
                    if (calculateRequest != null) {
                        ValidatedRequest validatedRequest = calculateRequestMapper.map(calculateRequest);
                        newState = evaluateState(validatedRequest);
                        contract.setCalcId(calcIdGenerator.generateCalcId(productService.getProductByName(productName)));
                        contract.setPremium(newState.getTotalPremium());
                        contract.setInsuredSum(newState.getInsuranceSum());
                        contract.setCalcDate(LocalDateTime.now());
                        contract.setStartDate(contractDatesService.startDate());
                        contract.setEndDate(newState.getEndDate());
                        contract.setParams(newState.getParameters());

                        // TODO надо получать кодировку не только при расчёте
                        if (contract.getNumber() == null || contract.getNumber().isEmpty()) {
                            contract.setNumber(contractNumberGenerator.generateContractNumber(newState.getProductNumberCode(), product));
                        }
                    }

                    contract.setInsured(clientService.updateOrCreateInsured(updateRequest.getInsured()));

                    return contractRepository.save(contract);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Contract with id=" + updateRequest.getPolicyId() + " not found"
                ));
    }

    @Override
    public Contract issue(IssueRequest issueRequest) throws Exception {
        Product product = productService.getProductByName(productName);

        Contract contract = contractRepository.findByIdAndProduct(issueRequest.getPolicyId(), product)
                .orElseThrow(() -> new RuntimeException(
                        "Contract with id=" + issueRequest.getPolicyId() + " not found"
                ));

        issueValidateService.validate(contract);
        ContractStateContext.init(contract)
                .getState()
                .toIssuedState();

        return contractRepository.save(contract);

    }

    private BaseTariffModel evaluateState(ValidatedRequest validatedRequest) {
        return (BaseTariffModel) calculationService.calculateTariffModel(validatedRequest);
    }
}
