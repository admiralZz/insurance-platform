package ru.virtusystems.domain.product.zachitadohoda20;

import lombok.RequiredArgsConstructor;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.domain.product.ContractService;
import ru.virtusystems.domain.io.CalculateRequest;
import ru.virtusystems.domain.io.IssueRequest;
import ru.virtusystems.domain.io.SaveRequest;
import ru.virtusystems.domain.io.UpdateRequest;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.types.ContractStatus;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.domain.port.repository.ContractRepository;
import ru.virtusystems.domain.product.ProductService;
import ru.virtusystems.domain.product.dms.DmsContractNumberGenerator;
import ru.virtusystems.domain.product.dms.io.DmsCalculateRequest;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;
import ru.virtusystems.domain.product.zachitadohoda20.io.ZachitaDohoda20CalculateRequest;
import ru.virtusystems.domain.product.zachitadohoda20.mapper.ZachitaDohoda20CalculateRequestMapper;
import ru.virtusystems.domain.product.zachitadohoda20.model.ZachitaDohoda20TariffModel;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ZachitaDohoda20ContractService implements ContractService {
    public static final String PRODUCT_NAME = "Защита дохода 2.0";

    private final ZachitaDohoda20CalculationService calculationService;
    private final ClientService clientService;
    private final ProductService productService;
    private final ZachitaDohoda20CalcIdGenerator calcIdGenerator;
    private final ZachitaDohoda20ContractNumberGenerator contractNumberGenerator;
    private final ZachitaDohoda20ContractDatesService contractDatesService;
    private final ZachitaDohoda20CalculateRequestMapper calculateRequestMapper;
    private final ContractRepository contractRepository;

    @Override
    public Product create() {
        return productService.getOrCreate(Product.builder()
                .name(PRODUCT_NAME)
                .description("Продукт страхования дохода при инвестиционных рисках")
                .build());
    }

    @Override
    public Contract calculate(CalculateRequest calculateRequest) {
        ZachitaDohoda20CalculateRequest dmsCalculateRequest = calculateRequestMapper.toZachitaDohoda20CalculateRequest(calculateRequest);
        ZachitaDohoda20TariffModel newState = calculationService.calculateTariffModel(dmsCalculateRequest);
        Product product = productService.getProductByName(PRODUCT_NAME);

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
        ZachitaDohoda20TariffModel newState;
        if (calculateRequest != null) {
            ZachitaDohoda20CalculateRequest dmsCalculateRequest = calculateRequestMapper.toZachitaDohoda20CalculateRequest(calculateRequest);
            newState = calculationService.calculateTariffModel(dmsCalculateRequest);
        } else {
            newState = ZachitaDohoda20TariffModel.builder().build();
        }
        Product product = productService.getProductByName(PRODUCT_NAME);

        return contractRepository.save(Contract.builder()
                .product(product)
                .calcId(calcIdGenerator.generateCalcId(productService.getProductByName(PRODUCT_NAME)))
                .number(contractNumberGenerator.generateContractNumber(newState, product))
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
        Product product = productService.getProductByName(PRODUCT_NAME);

        return contractRepository.findByIdAndProduct(updateRequest.getPolicyId(), product)
                .map(contract -> {
                    CalculateRequest calculateRequest = updateRequest.getCalcRequest();
                    ZachitaDohoda20TariffModel newState;
                    if (calculateRequest != null) {
                        ZachitaDohoda20CalculateRequest dmsCalculateRequest = calculateRequestMapper.toZachitaDohoda20CalculateRequest(calculateRequest);
                        newState = calculationService.calculateTariffModel(dmsCalculateRequest);
                        contract.setCalcId(calcIdGenerator.generateCalcId(productService.getProductByName(PRODUCT_NAME)));
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
        Product product = productService.getProductByName(PRODUCT_NAME);

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
