package ru.virtusystems.calculator;

// TODO доделать класс чтобы брал из реальной таблицы ОДЗ

import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.virtusystems.domain.model.types.ContractParameter;
import ru.virtusystems.domain.port.AccessibleTypesCollector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ExcelAccessibleTypesCollector implements AccessibleTypesCollector {
    private static final String MAIN_LIST_NAME = "ОДЗ";
    private static final String COLUMN_PARAM_NAME = "ОДЗ.ПарамНазв";
    private static final String COLUMN_PARAM_CODE = "ОДЗ.ПарамПолнКод";
    private static final String COLUMN_PARAM_TYPE = "ОДЗ.ПарамТип";
    private static final String COLUMN_PARAM_VALUE_CODE = "ОДЗ.ПарамЗначКод";
    private static final String COLUMN_PARAM_VALUE_NAME = "ОДЗ.ПарамЗначНазв";
    private static final String COLUMN_PARAM_AVAILABLE = "ОДЗ.ПарамЗначДопуст";
    private static final int HEADER_ROW = 18;
    private static final int START_PARAMS_ROW = HEADER_ROW + 4;

    private final Path pathToExcelFile;

    @Override
    public String getAccessibleType(ContractParameter contractParameter) {
        return Optional.ofNullable(getAccessibleTypes().get(contractParameter.getCode()))
                .map(map -> map.get((String) contractParameter.getInValue()))
                .orElse(null);
    }

    @Override
    public Map<String, String> getAccessibleTypesByCode(String code) {
        return Optional.ofNullable(getAccessibleTypes().get(code))
                .orElseThrow(() -> new RuntimeException("Не найдены ОДЗ для параметра с кодом '" + code + "'"));
    }

    @Override
    public Map<String, Map<String, String>> getAccessibleTypesMap() {
        return getAccessibleTypes();
    }

    private Map<String, Map<String, String>> getAccessibleTypes() {
        try (Workbook templateWb = getTemplate()) {

            Sheet sheet = templateWb.getSheet(MAIN_LIST_NAME);
            if (sheet == null) {
                throw new IllegalStateException("Не найден лист " + MAIN_LIST_NAME);
            }
            FormulaEvaluator evaluator = templateWb.getCreationHelper().createFormulaEvaluator();

            return readAccessibleTypes(sheet, evaluator)
                    .collect(Collectors.toMap(AccessibleType::getCode, v -> v.getAccessibleTypeValues().stream()
                            .filter(AccessibleType.AccessibleTypeValue::isAccessible)
                            .collect(Collectors.toMap(
                                    AccessibleType.AccessibleTypeValue::getCode,
                                    AccessibleType.AccessibleTypeValue::getName))));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<AccessibleType> readAccessibleTypes(Sheet sheet, FormulaEvaluator evaluator) {
        Map<String, Integer> headerColumnsMap = getHeaderColumnsMap(sheet);
        return readParameterRows(sheet)
                .map(row -> {
                    String name = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_NAME)));
                    String fullCode = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_CODE)));
                    if ((name == null || name.isBlank()) && (fullCode == null || fullCode.isBlank())) {
                        return null;
                    }

                    String type = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_TYPE)));
                    if (type == null || type.isBlank()) {
                        return null;
                    }
                    AccessibleType.AccessibleTypeBuilder accessibleTypeBuilder = AccessibleType.builder()
                            .code(fullCode)
                            .name(name);
                    Set<AccessibleType.AccessibleTypeValue> accessibleTypeValueSet = new HashSet<>();
                    for (int i = row.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                        Row valuesRow = sheet.getRow(i);
                        if (valuesRow == null) {
                            continue;
                        }
                        String valueCode = getStringCell(valuesRow.getCell(headerColumnsMap.get(COLUMN_PARAM_VALUE_CODE)));
                        String valueName = getStringCell(valuesRow.getCell(headerColumnsMap.get(COLUMN_PARAM_VALUE_NAME)));
                        Boolean accessible = evaluateValue(evaluator, valuesRow.getCell(headerColumnsMap.get(COLUMN_PARAM_AVAILABLE)));
                        if ((valueCode == null || valueCode.isBlank()) || (valueName == null || valueName.isBlank())) {
                            break;
                        }

                        accessibleTypeValueSet.add(
                                AccessibleType.AccessibleTypeValue.builder()
                                        .code(valueCode)
                                        .name(valueName)
                                        .accessible(accessible)
                                        .build());
                    }
                    if (accessibleTypeValueSet.isEmpty()) {
                        return null;
                    }
                    return accessibleTypeBuilder
                            .accessibleTypeValues(accessibleTypeValueSet)
                            .build();
                })
                .filter(Objects::nonNull);
    }

    private Map<String, Integer> getHeaderColumnsMap(Sheet sheet) {
        Row headerRow = sheet.getRow(HEADER_ROW);
        if (headerRow == null) {
            throw new IllegalStateException("Не найдены заголовки");
        }

        int nameCol = -1;
        int codeCol = -1;
        int typeCol = -1;
        int valueCodeCol = -1;
        int valueNameCol = -1;
        int availableCol = -1;


        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            Cell cell = headerRow.getCell(c);
            if (cell == null) continue;
            String text = cell.toString().trim();
            if (text.equalsIgnoreCase(COLUMN_PARAM_NAME)) {
                nameCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_CODE)) {
                codeCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_VALUE_CODE)) {
                valueCodeCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_VALUE_NAME)) {
                valueNameCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_AVAILABLE)) {
                availableCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_TYPE)) {
                typeCol = c;
            }
        }

        if (nameCol < 0 ||
                codeCol < 0 ||
                valueCodeCol < 0 ||
                valueNameCol < 0 ||
                typeCol < 0 ||
                availableCol < 0) {
            throw new IllegalStateException("Файл не соответствует ожидаемому формату. " +
                    "Не найдены обязательные колонки");
        }

        return Map.of(COLUMN_PARAM_NAME, nameCol,
                COLUMN_PARAM_CODE, codeCol,
                COLUMN_PARAM_VALUE_CODE, valueCodeCol,
                COLUMN_PARAM_VALUE_NAME, valueNameCol,
                COLUMN_PARAM_AVAILABLE, availableCol,
                COLUMN_PARAM_TYPE, typeCol);
    }

    private Stream<Row> readParameterRows(Sheet sheet) {
        return IntStream.range(START_PARAMS_ROW, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull);
    }

    private String getStringCell(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.toString();
            default -> null;
        };
    }

    private Boolean getBooleanCell(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        }
        return false;
    }

    private Boolean evaluateValue(FormulaEvaluator evaluator, Cell cell) {
        if (cell == null) return null;

        CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null) {
            return false;
        }
        if (cellValue.getCellType() == CellType.BOOLEAN) {
            return cellValue.getBooleanValue();
        }

        return false;
    }

    private Workbook getTemplate() throws IOException {
        return getWorkbook();
    }

    private Workbook getWorkbook() throws IOException {
        String filename = pathToExcelFile.getFileName().toString();
        InputStream fis = Files.newInputStream(pathToExcelFile);
        if (filename.toLowerCase().endsWith("xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (filename.toLowerCase().endsWith("xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + filename);
        }
    }

}
