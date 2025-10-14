package ru.virtusystems.platform.conf.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.calculator.ExcelTariffDescriptor;
import ru.virtusystems.domain.contract.StandardOfficeContractService;
import ru.virtusystems.domain.contract.StandardPartnerContractService;
import ru.virtusystems.domain.generator.StandardCalcIdGenerator;
import ru.virtusystems.domain.generator.StandardContractNumberGenerator;
import ru.virtusystems.domain.port.client.ClientService;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.product.dms.*;
import ru.virtusystems.domain.product.dms.mapper.DmsCalculateRequestMapper;
import ru.virtusystems.domain.validation.StandardIssueValidateService;
import ru.virtusystems.platform.conf.properties.ProductProperties;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
@ConditionalOnProperty(
        prefix = "app.products." + ProductProperties.PRODUCT_CODE_DMS,
        name = "enabled",
        havingValue = "true"
)
public class DmsProductConfiguration {

    private final ProductProperties.ProductConfig productConfig;

    public DmsProductConfiguration(ProductProperties productProperties) {
        this.productConfig = productProperties.getProductConfig(ProductProperties.PRODUCT_CODE_DMS);
    }


    @Bean
    public DmsProductFacade productFacade(ClientService clientService,
                                          ProductService productService,
                                          ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                          CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                          ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {


        var tariffConfig = productConfig.tariff();
        var tariffDescriptor = new ExcelTariffDescriptor(
                Path.of(tariffConfig.path()),
                tariffConfig.showEmptyValuesParams());
        var datesService = new DmsContractDatesService();
        var settingTablesService = new DmsSettingTablesService();
        var calcValidateService = new DmsCalcValidateService(tariffDescriptor);
        var issueValidateService = new StandardIssueValidateService();
        var calculationService = new DmsCalculationService(tariffDescriptor, datesService, settingTablesService, calcValidateService);
        var calcIdGenerator = new StandardCalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new StandardContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new DmsCalculateRequestMapper();

        var standardContractService = new StandardPartnerContractService(
                productConfig.name(),
                calculationService,
                issueValidateService,
                clientService,
                productService,
                calcIdGenerator,
                contractNumberGenerator,
                datesService,
                requestMapper,
                contractRepositoryJpaAdapter
        );
        var officeContractService = new StandardOfficeContractService(tariffDescriptor, settingTablesService);

        return new DmsProductFacade(productConfig.name(),
                productConfig.description(),
                productService,
                standardContractService,
                officeContractService);
    }
}
