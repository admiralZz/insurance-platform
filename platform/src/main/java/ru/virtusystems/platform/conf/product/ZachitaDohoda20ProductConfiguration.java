package ru.virtusystems.platform.conf.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.calculator.ExcelAccessibleTypesCollector;
import ru.virtusystems.calculator.ExcelTariffEvaluator;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.domain.product.ProductService;
import ru.virtusystems.domain.product.dms.*;
import ru.virtusystems.domain.product.dms.mapper.DmsCalculateRequestMapper;
import ru.virtusystems.domain.product.zachitadohoda20.*;
import ru.virtusystems.domain.product.zachitadohoda20.mapper.ZachitaDohoda20CalculateRequestMapper;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
public class ZachitaDohoda20ProductConfiguration {

    private static final String PRODUCT_NAME = "Защита дохода 2.0";

    private final Path pathToTariffEvaluator = Path.of(
            "/home/andrey/packages/insurance-platform/calculator/src/test/resources",
            "Zasita_dohoda_2.0_ver1_rev61.xls");

    @Bean(PRODUCT_NAME)
    public ZachitaDohoda20ContractService contractService(ClientService clientService,
                                                          ProductService productService,
                                                          ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                                          CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                                          ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {
        var accessibleTypesCollector = new ExcelAccessibleTypesCollector();
        var tariffEvaluator = new ExcelTariffEvaluator(pathToTariffEvaluator, accessibleTypesCollector);
        var datesService = new ZachitaDohoda20ContractDatesService();
        var settingTablesService = new ZachitaDohoda20SettingTablesService();
        var calcValidateService = new ZachitaDohoda20CalcValidateService(accessibleTypesCollector);
        var calculationService = new ZachitaDohoda20CalculationService(tariffEvaluator, datesService, settingTablesService, calcValidateService);
        var calcIdGenerator = new ZachitaDohoda20CalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new ZachitaDohoda20ContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new ZachitaDohoda20CalculateRequestMapper();

        return new ZachitaDohoda20ContractService(
                calculationService,
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
