package ru.virtusystems.domain.contract;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.calculation.CalculationService;
import ru.virtusystems.domain.dates.ContractDatesService;
import ru.virtusystems.domain.generators.CalcGenerator;
import ru.virtusystems.domain.generators.ContractNumberGenerator;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.mapper.CalculateRequestMapper;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.model.types.ContractStatus;
import ru.virtusystems.domain.client.ClientService;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.product.ProductService;
import ru.virtusystems.domain.validation.ValidatedRequest;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class StandardPartnerContractService implements PartnerContractService {

    private final String productName;
    private final CalculationService calculationService;
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
        BaseTariffModel newState = (BaseTariffModel)
                calculationService.calculateTariffModel(validatedRequest);
        Product product = productService.getProductByName(productName);

        return contractRepository.save(Contract.builder()
                .product(product)
                .calcId(calcIdGenerator.generateCalcId(product))
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
        BaseTariffModel newState;
        if (calculateRequest != null) {
            ValidatedRequest validatedRequest = calculateRequestMapper.map(calculateRequest);
            newState = (BaseTariffModel) calculationService.calculateTariffModel(validatedRequest);
        } else {
            newState = BaseTariffModel.builder().build();
        }
        Product product = productService.getProductByName(productName);

        return contractRepository.save(Contract.builder()
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
                // TODO можно сделать StateMachine для контроля переходов между статусами
                .status(ContractStatus.PROJECT)
                .build());
    }

    @Override
    public Contract update(UpdateRequest updateRequest) {
        Product product = productService.getProductByName(productName);

        return contractRepository.findByIdAndProduct(updateRequest.getPolicyId(), product)
                .map(contract -> {
                    CalculateRequest calculateRequest = updateRequest.getCalcRequest();
                    BaseTariffModel newState;
                    if (calculateRequest != null) {
                        ValidatedRequest dmsCalculateRequest = calculateRequestMapper.map(calculateRequest);
                        newState = (BaseTariffModel)
                                calculationService.calculateTariffModel(dmsCalculateRequest);
                        contract.setCalcId(calcIdGenerator.generateCalcId(productService.getProductByName(productName)));
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
        Product product = productService.getProductByName(productName);

        return contractRepository.findByIdAndProduct(issueRequest.getPolicyId(), product)
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
