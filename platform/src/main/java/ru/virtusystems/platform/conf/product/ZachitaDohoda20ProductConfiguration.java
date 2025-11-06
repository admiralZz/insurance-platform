package ru.virtusystems.platform.conf.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

@Configuration
@ConditionalOnProperty(
        prefix = "app.products." + ProductProperties.PRODUCT_CODE_ZACHITA_DOHODA_2_0,
        name = "enabled",
        havingValue = "true"
)
@Profile("!test")
public class ZachitaDohoda20ProductConfiguration {

    private final ProductProperties.ProductConfig productConfig;

    public ZachitaDohoda20ProductConfiguration(ProductProperties productProperties) {
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
    public ExcelTariffDescriptor tariffDescriptor() {
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
    public ZachitaDohoda20ContractDatesService contractDatesService() {
        return new ZachitaDohoda20ContractDatesService();
    }

    @Bean
    public ZachitaDohoda20CalculationService calculationService(TariffDescriptor tariffDescriptor,
                                                                ZachitaDohoda20SettingTablesService settingTablesService,
                                                                ZachitaDohoda20ContractDatesService datesService) {
        var calcValidateService = new ZachitaDohoda20CalcValidateService(tariffDescriptor);
        return new ZachitaDohoda20CalculationService(tariffDescriptor,
                datesService,
                settingTablesService,
                calcValidateService);
    }

    @Bean
    public StandardPartnerContractService partnerContractService(ZachitaDohoda20ContractDatesService datesService,
                                                                 ZachitaDohoda20CalculationService calculationService,
                                                                 ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                                                 CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                                                 ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter,
                                                                 ClientService clientService,
                                                                 ProductService productService) {
        var issueValidateService = new StandardIssueValidateService();
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
