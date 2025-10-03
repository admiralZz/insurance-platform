package ru.virtusystems.calculator;

// TODO доделать класс чтобы брал из реальной таблицы ОДЗ

import lombok.Getter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import ru.virtusystems.calculator.port.AccessibleTypesCollector;

import java.util.Map;

public class SimpleMapAccessibleTypesCollector implements AccessibleTypesCollector {
    // TODO удалить после доработки
    private final Map<String, String> programs = Map.of("Лайт", "05",
            "Стандарт", "10",
            "Премиум", "20");

    private final Map<String, String> programm = Map.of(
            "Пенсионный (автопролонгация)", "07",
            "Стабильный", "06",
            "Персональный", "05",
            "Новый Пенсионный", "04",
            "Все возможно", "03",
            "Все удобно", "02",
            "Все просто", "01",
            "Стандартный", "075C",
            "Базовый", "08",
            "Премиум", "09"
    );

    private final Map<String, String> insurancePeriod = Map.of(
            "90 дней", "090",
            "100 дней", "100",
            "121 день", "121",
            "150 дней", "150",
            "181 день", "181",
            "200 дней", "200",
            "367 дней", "367",
            "395 дней", "395",
            "397 дней", "397"
    );

    private final Map<String, String> percentType = Map.of(
            "Без капитализации", "02",
            "С капитализацией", "01"
    );
    private final Map<String, String> paymentMethods = Map.of(
            "Оплата через кассу", "10",
            "Оплата по счету", "20",
            "Онлайн", "30"
    );

    private final Map<String, String> periods = Map.of("1 год", "01",
            "2 года", "02",
            "3 года", "03",
            "4 года", "04",
            "5 лет", "05",
            "6 лет", "06",
            "7 лет", "07");

    @Getter
    private final Map<String, Map<String, String>> accessibleTypes = Map.of(
            "dogovor.programma", programs,
            "dogovor.programm", programm,
            "dogovor.insurancePeriod", insurancePeriod,
            "dogovor.tipprocentov", percentType,
            "dogovor.sposobOplati", paymentMethods,
            "dogovor.SrokStrahGod", periods
    );


    @Override
    public Map<String, String> getAccessibleTypesByCode(Workbook workbook, String code) {
        return Map.of();
    }

    @Override
    public Map<String, Map<String, String>> getAccessibleTypes(Workbook workbook, FormulaEvaluator evaluator) {
        return Map.of();
    }

    @Override
    public Map<String, Map<String, String>> getAllTypes(Workbook workbook) {
        return Map.of();
    }
}
