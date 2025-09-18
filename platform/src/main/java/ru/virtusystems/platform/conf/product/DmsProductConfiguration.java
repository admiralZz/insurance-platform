package ru.virtusystems.platform.conf.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.calculator.ExcelAccessibleTypesCollector;
import ru.virtusystems.calculator.ExcelTariffEvaluator;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.domain.product.dms.*;
import ru.virtusystems.domain.product.dms.mapper.DmsCalculateRequestMapper;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
public class DmsProductConfiguration {

    private static final String PRODUCT_NAME = "ДМС при ДТП";

    private final Path pathToTariffEvaluator = Path.of(
            "/home/andrey/packages/insurance-platform/calculator/src/test/resources",
            "DMS_pri_DTP_ver1_rev25.xls");

    @Bean(PRODUCT_NAME)
    public DmsContractService contractService(ClientService clientService,
                                              ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                              CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                              ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {
        var accessibleTypesCollector = new ExcelAccessibleTypesCollector();
        var tariffEvaluator = new ExcelTariffEvaluator(pathToTariffEvaluator, accessibleTypesCollector);
        var datesService = new DmsContractDatesService();
        var settingTablesService = new DmsSettingTablesService();
        var calcValidateService = new DmsCalcValidateService(accessibleTypesCollector);
        var calculationService = new DmsCalculationService(tariffEvaluator, datesService, settingTablesService, calcValidateService);
        var calcIdGenerator = new DmsCalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new DmsContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new DmsCalculateRequestMapper();

        return new DmsContractService(
                calculationService,
                clientService,
                calcIdGenerator,
                contractNumberGenerator,
                datesService,
                requestMapper,
                contractRepositoryJpaAdapter
        );
    }
}
