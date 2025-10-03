package ru.virtusystems.domain.product.dms;


import ru.virtusystems.domain.model.evaluator.TariffModel;
import ru.virtusystems.domain.port.setting.SettingTablesService;
import ru.virtusystems.domain.product.dms.model.DmsTariffModel;

import java.math.BigDecimal;
import java.util.Map;

public class DmsSettingTablesService implements SettingTablesService {

    private Map<String, BigDecimal> insuranceSum;
    private Map<String, BigDecimal> insurancePremium;

    public DmsSettingTablesService() {
        init();
    }

    public void init() {
        insuranceSum = Map.of("Лайт", BigDecimal.valueOf(350000),
                "Стандарт", BigDecimal.valueOf(600000),
                "Премиум", BigDecimal.valueOf(1000000));

        insurancePremium = Map.of("Лайт", BigDecimal.valueOf(10000),
                "Стандарт", BigDecimal.valueOf(16500),
                "Премиум", BigDecimal.valueOf(21200));
    }

    public BigDecimal getInsuranceSumByProgramName(String programName) {
        boolean contains = insuranceSum.containsKey(programName);
        if (contains) {
            return insuranceSum.get(programName);
        }
        throw new RuntimeException("Страховая сумма по программе '" + programName + "' не найдена");
    }

    public BigDecimal getPremiumByProgramName(String programName) {
        boolean contains = insurancePremium.containsKey(programName);
        if (contains) {
            return insurancePremium.get(programName);
        }
        throw new RuntimeException("Премия по программе '" + programName + "' не найдена");
    }

    @Override
    public TariffModel getDefaultSettingsTables() {
        return DmsTariffModel.builder()
                .build();
    }
}
