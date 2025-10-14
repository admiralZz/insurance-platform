package ru.virtusystems.domain.product.zachitadohoda20;


import ru.virtusystems.domain.model.evaluator.TariffModel;
import ru.virtusystems.domain.port.setting.SettingTablesService;
import ru.virtusystems.domain.product.zachitadohoda20.model.ZachitaDohoda20TariffModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ZachitaDohoda20SettingTablesService implements SettingTablesService {

    private Map<String, BigDecimal> insuranceSum;
    private Map<String, BigDecimal> insurancePremium;
    private Map<String, Boolean> accessibleProgram;
    private Map<String, Boolean> accessiblePeriodDays;
    private Map<String, Boolean> accessiblePayment;

    public ZachitaDohoda20SettingTablesService() {
        init();
    }


    @Override
    public TariffModel getDefaultSettingsTables() {

        return ZachitaDohoda20TariffModel.builder()
                .citizenship("Россия")
                .accessiblePaymentMethodFromSettings(getAccessiblePaymentMethod())
                .accessiblePeriodFromSettings(getAccessiblePeriodDays())
                .accessibleProgramFromSettings(getAccessibleProgram())
                .build();
    }

    public void init() {
        insuranceSum = new HashMap<>();
        insurancePremium = new HashMap<>();

        // Access flags (from screenshot). You can adjust if product config changes.
        accessibleProgram = Map.of(
                "Стандартный", true,
                "Базовый", true,
                "Премиум", true
        );

        accessiblePeriodDays = Map.of(
                "90 дней", true,
                "121 день", true,
                "181 день", true,
                "367 дней", true
        );

        accessiblePayment = Map.of(
                "Оплата по счету", true,
                "Онлайн", true
        );

        // Program-period tables from screenshot
        putSumAndPremium("Стандартный", "90", 50_000, 500);
        putSumAndPremium("Стандартный", "121", 50_000, 700);
        putSumAndPremium("Стандартный", "181", 50_000, 1_000);
        putSumAndPremium("Стандартный", "367", 50_000, 2_000);

        putSumAndPremium("Базовый", "90", 150_000, 1_500);
        putSumAndPremium("Базовый", "121", 150_000, 2_000);
        putSumAndPremium("Базовый", "181", 150_000, 3_000);
        putSumAndPremium("Базовый", "367", 150_000, 6_000);

        putSumAndPremium("Премиум", "90", 300_000, 3_000);
        putSumAndPremium("Премиум", "121", 300_000, 4_000);
        putSumAndPremium("Премиум", "181", 300_000, 5_000);
        putSumAndPremium("Премиум", "367", 300_000, 10_000);
    }

    public BigDecimal getInsuranceSumByProgramNameAndPeriod(String programName, String periodDays) {
        String normalizedPeriod = normalizePeriod(periodDays);
        validateAccessible(programName, normalizedPeriod);
        BigDecimal sum = insuranceSum.get(key(programName, normalizedPeriod));
        if (sum != null) {
            return sum;
        }
        throw new RuntimeException("Страховая сумма по программе '" + programName + "' и сроку '" + periodDays + "' не найдена");
    }

    public BigDecimal getPremiumByProgramNameAndPeriod(String programName, String periodDays) {
        String normalizedPeriod = normalizePeriod(periodDays);
        validateAccessible(programName, normalizedPeriod);
        BigDecimal premium = insurancePremium.get(key(programName, normalizedPeriod));
        if (premium != null) {
            return premium;
        }
        throw new RuntimeException("Премия по программе '" + programName + "' и сроку '" + periodDays + "' не найдена");
    }

    public String getAccessiblePeriodDays() {
        return getAccessibleMap(accessiblePeriodDays);
    }
    public String getAccessibleProgram() {
        return getAccessibleMap(accessibleProgram);
    }
    public String getAccessiblePaymentMethod() {
        return getAccessibleMap(accessiblePayment);
    }

    private String getAccessibleMap(Map<String, Boolean> map) {
        return map.entrySet().stream()
                .filter(Map.Entry::getValue) // оставляем только те, где value == true
                .map(Map.Entry::getKey)      // берем ключ
                .collect(Collectors.joining(";")) + ";"; // объединяем через ";"
    }

    private void putSumAndPremium(String programName, String periodDaysNumeric, int sumRub, int premiumRub) {
        String k = key(programName, periodDaysNumeric);
        insuranceSum.put(k, BigDecimal.valueOf(sumRub));
        insurancePremium.put(k, BigDecimal.valueOf(premiumRub));
    }

    private String key(String programName, String normalizedPeriod) {
        return programName + "|" + normalizedPeriod;
    }

    private String normalizePeriod(String periodDays) {
        if (periodDays == null) {
            return "";
        }
        String digits = periodDays.replaceAll("[^0-9]", "");
        return digits;
    }

    private void validateAccessible(String programName, String normalizedPeriod) {
        if (accessibleProgram != null && Boolean.FALSE.equals(accessibleProgram.getOrDefault(programName, true))) {
            throw new RuntimeException("Программа '" + programName + "' недоступна");
        }
        if (accessiblePeriodDays != null && Boolean.FALSE.equals(accessiblePeriodDays.getOrDefault(normalizedPeriod, true))) {
            throw new RuntimeException("Срок '" + normalizedPeriod + "' дней недоступен");
        }
    }

}
