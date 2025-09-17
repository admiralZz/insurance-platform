package ru.virtusystems.platform.conf.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.calculator.ExcelAccessibleTypesCollector;
import ru.virtusystems.calculator.ExcelTariffEvaluator;
import ru.virtusystems.domain.client.StandardClientService;
import ru.virtusystems.domain.port.AccessibleTypesCollector;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.domain.port.TariffEvaluator;
import ru.virtusystems.domain.product.dms.*;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.InsuredRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
public class DmsProductConfiguration {

    private final Path pathToTariffEvaluator = Path.of(
            "/home/andrey/packages/insurance-platform/calculator/src/test/resources",
            "DMS_pri_DTP_ver1_rev25.xls");

    @Bean
    public DmsContractService contractService(DmsCalculationService calculationService,
                                              ClientService clientService,
                                              DmsCalcIdGenerator calcIdGenerator,
                                              DmsContractNumberGenerator contractNumberGenerator,
                                              DmsContractDatesService contractDatesService,
                                              ContractRepositoryJpaAdapter contractRepositoryJpaAdapter) {
        return new DmsContractService(calculationService, clientService, calcIdGenerator,
                contractNumberGenerator, contractDatesService, contractRepositoryJpaAdapter);
    }

    @Bean
    public DmsCalculationService calculationService(TariffEvaluator tariffEvaluator,
                                                    DmsContractDatesService datesService,
                                                    DmsSettingTablesService settingTablesService,
                                                    DmsCalcValidateService calcValidateService) {

        return new DmsCalculationService(tariffEvaluator, datesService, settingTablesService, calcValidateService);
    }

    @Bean
    public ClientService clientService(InsuredRepositoryJpaAdapter insuredRepositoryJpaAdapter) {
        return new StandardClientService(insuredRepositoryJpaAdapter);
    }

    @Bean
    public DmsContractDatesService contractDatesService() {
        return new DmsContractDatesService();
    }

    @Bean
    public DmsCalcValidateService calcValidateService(AccessibleTypesCollector accessibleTypesCollector) {
        return new DmsCalcValidateService(accessibleTypesCollector);
    }

    @Bean
    public TariffEvaluator tariffEvaluator(AccessibleTypesCollector accessibleTypesCollector) {
        return new ExcelTariffEvaluator(pathToTariffEvaluator,
                accessibleTypesCollector);
    }

    @Bean
    public DmsSettingTablesService settingTablesService() {
        return new DmsSettingTablesService();
    }

    @Bean
    public AccessibleTypesCollector accessibleTypesCollector() {
        return new ExcelAccessibleTypesCollector();
    }

    @Bean
    public DmsCalcIdGenerator calcIdGenerator(CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter) {
        return new DmsCalcIdGenerator(calcCounterRepositoryJpaAdapter);
    }

    @Bean
    public DmsContractNumberGenerator contractNumberGenerator(ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {
        return new DmsContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
    }
}
