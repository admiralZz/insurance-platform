package ru.virtusystems.platform.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.virtusystems.calculator.ExcelTariffDescriptor;
import ru.virtusystems.domain.contract.StandardOfficeContractService;
import ru.virtusystems.domain.contract.StandardPartnerContractService;
import ru.virtusystems.domain.generator.StandardCalcIdGenerator;
import ru.virtusystems.domain.generator.StandardContractNumberGenerator;
import ru.virtusystems.domain.port.TariffDescriptor;
import ru.virtusystems.domain.port.client.ClientService;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.product.zachitadohoda20.*;
import ru.virtusystems.domain.product.zachitadohoda20.mapper.ZachitaDohoda20CalculateRequestMapper;
import ru.virtusystems.domain.validation.StandardIssueValidateService;
import ru.virtusystems.platform.conf.properties.ProductProperties;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@TestConfiguration
@ConditionalOnProperty(
        prefix = "app.products." + ProductProperties.PRODUCT_CODE_ZACHITA_DOHODA_2_0,
        name = "enabled",
        havingValue = "true"
)
public class TestZachitaDohoda20ProductConfiguration {

    private final ProductProperties.ProductConfig productConfig;

    public TestZachitaDohoda20ProductConfiguration(ProductProperties productProperties) {
        this.productConfig = productProperties.getProductConfig(ProductProperties.PRODUCT_CODE_ZACHITA_DOHODA_2_0);
    }
    @Bean
    public ZachitaDohoda20ProductFacade contractService(TariffDescriptor tariffDescriptor,
                                                        ProductService productService,
                                                        StandardPartnerContractService standardContractService,
                                                        ZachitaDohoda20SettingTablesService settingTablesService) {

        var officeContractService = new StandardOfficeContractService(tariffDescriptor, settingTablesService);

        return new ZachitaDohoda20ProductFacade(productConfig.name(),
                productConfig.description(),
                productService,
                standardContractService,
                officeContractService);
    }

    @Bean
    public TariffDescriptor tariffDescriptor() {
        var tariffConfig = productConfig.tariff();
        return new ExcelTariffDescriptor(
                Path.of(tariffConfig.path()),
                tariffConfig.showEmptyValuesParams());
    }

    @Bean
    public ZachitaDohoda20SettingTablesService settingTablesService() {
        return new ZachitaDohoda20SettingTablesService();
    }

    @Bean
    public StandardPartnerContractService partnerContractService(TariffDescriptor tariffDescriptor,
                                                         ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                                         CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                                         ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter,
                                                         ClientService clientService,
                                                         ProductService productService,
                                                         ZachitaDohoda20SettingTablesService settingTablesService) {
        var datesService = new ZachitaDohoda20ContractDatesService();
        var calcValidateService = new ZachitaDohoda20CalcValidateService(tariffDescriptor);
        var issueValidateService = new StandardIssueValidateService();
        var calculationService = new ZachitaDohoda20CalculationService(tariffDescriptor,
                datesService,
                settingTablesService,
                calcValidateService);
        var calcIdGenerator = new StandardCalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new StandardContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new ZachitaDohoda20CalculateRequestMapper();

        return new StandardPartnerContractService(
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
    }
}
