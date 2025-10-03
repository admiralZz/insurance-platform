package ru.virtusystems.platform.conf.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.calculator.ExcelAccessibleTypesCollector;
import ru.virtusystems.calculator.ExcelTariffDescriptor;
import ru.virtusystems.calculator.ExcelTariffEvaluator;
import ru.virtusystems.domain.contract.StandardPartnerContractService;
import ru.virtusystems.domain.contract.StandardOfficeContractService;
import ru.virtusystems.domain.generator.StandardCalcIdGenerator;
import ru.virtusystems.domain.generator.StandardContractNumberGenerator;
import ru.virtusystems.domain.port.TariffDescriptor;
import ru.virtusystems.domain.port.client.ClientService;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.product.zachitadohoda20.*;
import ru.virtusystems.domain.product.zachitadohoda20.mapper.ZachitaDohoda20CalculateRequestMapper;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
public class ZachitaDohoda20ProductConfiguration {

    private final Path pathToTariffDescriptor = Path.of(
            "/home/andrey/packages/insurance-platform/calculator/src/test/resources",
            "Zasita_dohoda_2.0_ver1_rev61.xls");

    @Bean(ZachitaDohoda20ProductFacade.PRODUCT_NAME)
    public ZachitaDohoda20ProductFacade contractService(ClientService clientService,
                                                          ProductService productService,
                                                          ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                                          CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                                          ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {
        var tariffDescriptor = new ExcelTariffDescriptor(pathToTariffDescriptor);
        var datesService = new ZachitaDohoda20ContractDatesService();
        var settingTablesService = new ZachitaDohoda20SettingTablesService();
        var calcValidateService = new ZachitaDohoda20CalcValidateService(tariffDescriptor);
        var calculationService = new ZachitaDohoda20CalculationService(tariffDescriptor, datesService, settingTablesService, calcValidateService);
        var calcIdGenerator = new StandardCalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new StandardContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new ZachitaDohoda20CalculateRequestMapper();

        var standardContractService = new StandardPartnerContractService(
                ZachitaDohoda20ProductFacade.PRODUCT_NAME,
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

        return new ZachitaDohoda20ProductFacade(productService, standardContractService, officeContractService);
    }
}
