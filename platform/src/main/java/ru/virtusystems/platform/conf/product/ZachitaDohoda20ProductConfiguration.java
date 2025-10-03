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
import ru.virtusystems.domain.product.zachitadohoda20.*;
import ru.virtusystems.domain.product.zachitadohoda20.mapper.ZachitaDohoda20CalculateRequestMapper;
import ru.virtusystems.platform.conf.properties.ProductProperties;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
@ConditionalOnProperty(
        prefix = "app.products." + ProductProperties.PRODUCT_CODE_ZACHITA_DOHODA_2_0,
        name = "enabled",
        havingValue = "true"
)
public class ZachitaDohoda20ProductConfiguration {

    private final ProductProperties.ProductConfig productConfig;

    public ZachitaDohoda20ProductConfiguration(ProductProperties productProperties) {
        this.productConfig = productProperties.getProductConfig(ProductProperties.PRODUCT_CODE_ZACHITA_DOHODA_2_0);
    }
    @Bean
    public ZachitaDohoda20ProductFacade contractService(ClientService clientService,
                                                          ProductService productService,
                                                          ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                                          CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                                          ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {
        var tariffConfig = productConfig.tariff();
        var tariffDescriptor = new ExcelTariffDescriptor(
                Path.of(tariffConfig.path()),
                tariffConfig.showEmptyValuesParams());
        var datesService = new ZachitaDohoda20ContractDatesService();
        var settingTablesService = new ZachitaDohoda20SettingTablesService();
        var calcValidateService = new ZachitaDohoda20CalcValidateService(tariffDescriptor);
        var calculationService = new ZachitaDohoda20CalculationService(tariffDescriptor,
                datesService,
                settingTablesService,
                calcValidateService);
        var calcIdGenerator = new StandardCalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new StandardContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new ZachitaDohoda20CalculateRequestMapper();

        var standardContractService = new StandardPartnerContractService(
                productConfig.name(),
                calculationService,
                clientService,
                productService,
                calcIdGenerator,
                contractNumberGenerator,
                datesService,
                requestMapper,
                contractRepositoryJpaAdapter
        );
        var officeContractService = new StandardOfficeContractService(tariffDescriptor, settingTablesService);

        return new ZachitaDohoda20ProductFacade(productConfig.name(),
                productConfig.description(),
                productService,
                standardContractService,
                officeContractService);
    }
}
