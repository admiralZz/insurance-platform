package ru.virtusystems.calculator;

// TODO доделать класс чтобы брал из реальной таблицы ОДЗ

import ru.virtusystems.domain.port.AccessibleTypesCollector;
import ru.virtusystems.domain.model.types.ContractParameter;

import java.util.Map;
import java.util.Optional;

public class ExcelAccessibleTypesCollector implements AccessibleTypesCollector {
    private static final String MAIN_LIST_NAME = "ОДЗ";
    private static final String COLUMN_PARAM_NAME = "ПарамПрод.Назв";

    // TODO удалить после доработки
    private final Map<String, String> programs = Map.of("Лайт", "05",
            "Стандарт", "10",
            "Премиум", "20");

    private final Map<String, String> periods = Map.of("1 год", "01",
            "2 года", "02",
            "3 года", "03",
            "4 года", "04",
            "5 лет", "05",
            "6 лет", "06",
            "7 лет", "07");

    private final Map<String, Map<String, String>> accessibleTypes = Map.of(
            "dogovor.programma", programs,
            "dogovor.SrokStrahGod", periods);


    @Override
    public String getAccessibleType(ContractParameter contractParameter) {
        return Optional.ofNullable(accessibleTypes.get(contractParameter.getCode()))
                .map(map -> map.get((String) contractParameter.getInValue()))
                .orElse(null);
    }

    @Override
    public Map<String, String> getAccessibleTypesByCode(String code) {
        return Optional.ofNullable(accessibleTypes.get(code))
                .orElseThrow(() -> new RuntimeException("Не найдены ОДЗ для параметра с кодом '" + code + "'"));
    }

}
