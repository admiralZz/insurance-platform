package ru.virtusystems.platform.conf.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.calculator.ExcelTariffDescriptor;
import ru.virtusystems.calculator.ExcelTariffEvaluator;
import ru.virtusystems.calculator.SimpleMapAccessibleTypesCollector;
import ru.virtusystems.domain.port.client.ClientService;
import ru.virtusystems.domain.contract.StandardPartnerContractService;
import ru.virtusystems.domain.contract.StandardOfficeContractService;
import ru.virtusystems.domain.generator.StandardCalcIdGenerator;
import ru.virtusystems.domain.generator.StandardContractNumberGenerator;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.product.dms.*;
import ru.virtusystems.domain.product.dms.mapper.DmsCalculateRequestMapper;
import ru.virtusystems.platform.database.repository.adapter.CalcCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractNumberCounterRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ContractRepositoryJpaAdapter;

import java.nio.file.Path;

@Configuration
public class DmsProductConfiguration {

    private final Path pathToTariffDescriptor = Path.of(
            "/home/andrey/packages/insurance-platform/calculator/src/test/resources",
            "DMS_pri_DTP_ver1_rev25.xls");

    @Bean(DmsProductFacade.PRODUCT_NAME)
    public DmsProductFacade contractService(ClientService clientService,
                                              ProductService productService,
                                              ContractRepositoryJpaAdapter contractRepositoryJpaAdapter,
                                              CalcCounterRepositoryJpaAdapter calcCounterRepositoryJpaAdapter,
                                              ContractNumberCounterRepositoryJpaAdapter contractNumberCounterRepositoryJpaAdapter) {
        var tariffDescriptor = new ExcelTariffDescriptor(pathToTariffDescriptor);
        var datesService = new DmsContractDatesService();
        var settingTablesService = new DmsSettingTablesService();
        var calcValidateService = new DmsCalcValidateService(tariffDescriptor);
        var calculationService = new DmsCalculationService(tariffDescriptor, datesService, settingTablesService, calcValidateService);
        var calcIdGenerator = new StandardCalcIdGenerator(calcCounterRepositoryJpaAdapter);
        var contractNumberGenerator = new StandardContractNumberGenerator(contractNumberCounterRepositoryJpaAdapter);
        var requestMapper = new DmsCalculateRequestMapper();

        var standardContractService = new StandardPartnerContractService(
                DmsProductFacade.PRODUCT_NAME,
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

        return new DmsProductFacade(productService, standardContractService, officeContractService);
    }
}
