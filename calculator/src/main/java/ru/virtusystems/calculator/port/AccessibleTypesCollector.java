package ru.virtusystems.calculator.port;


import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public interface AccessibleTypesCollector {
    Map<String, String> getAccessibleTypesByCode(Workbook workbook, String code);
    Map<String, Map<String, String>> getAccessibleTypes(Workbook workbook, FormulaEvaluator evaluator);
    Map<String, Map<String, String>> getAllTypes(Workbook workbook);
}
