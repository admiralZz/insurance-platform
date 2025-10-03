package ru.virtusystems.calculator;

import org.apache.poi.ss.usermodel.*;
import ru.virtusystems.calculator.mapper.ContractParametersMapper;
import ru.virtusystems.calculator.port.TariffEvaluator;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExcelTariffEvaluator implements TariffEvaluator {
    private static final String MAIN_LIST_NAME = "ПараметрыПродукта";
    private static final String COLUMN_PARAM_NAME = "ПарамПрод.Назв";
    private static final String COLUMN_PARAM_CODE = "ПарамПрод.ПолнКод";
    private static final String COLUMN_PARAM_VALUE = "ПарамПрод.ЗначИсхНазв";
    private static final String COLUMN_PARAM_DICT_CODE = "ПарамПрод.ЗначИсхКод";
    private static final String COLUMN_PARAM_OUTPUT = "ПарамПрод.ЗначВычНазв";
    private static final String COLUMN_PARAM_FINAL_OUTPUT = "ПарамПрод.ЗначОконч";
    private static final String COLUMN_PARAM_TYPE = "ПарамПрод.Тип";
    private static final int HEADER_ROW = 9;
    private static final int START_PARAMS_ROW = HEADER_ROW + 4;

    private final ContractParametersMapper contractParametersMapper;
    private final boolean showEmptyValueParameters;

    public ExcelTariffEvaluator(ContractParametersMapper contractParametersMapper,
                                boolean showEmptyValueParameters) {
        this.contractParametersMapper = contractParametersMapper;
        this.showEmptyValueParameters = showEmptyValueParameters;
    }

    @Override
    public TariffEvaluationState evaluateState(Workbook workbook, TariffEvaluationState inputState) {
        try {
            List<IOParameter> ioParameters = evaluateAndGetIOParameters(workbook,
                    contractParametersMapper.mapToInputParameters(
                            inputState.getParameters()));
            return TariffEvaluationState.builder()
                    .parameters(contractParametersMapper.mapToContractParameters(ioParameters))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IOParameter> evaluateAndGetIOParameters(Workbook workbook, List<IOParameter> inputParameters) throws IOException {
        Sheet sheet = workbook.getSheet(MAIN_LIST_NAME);
        if (sheet == null) {
            throw new IllegalStateException("Не найден лист " + MAIN_LIST_NAME);
        }

        inputParameters.forEach(inputParameter -> setValueByCode(sheet, inputParameter.fullCode(), inputParameter.inValue(), inputParameter.dictValue()));
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        return readIOParameters(sheet, evaluator);
    }

    @Override
    public void setParams(Workbook workbook, List<IOParameter> inputParameters) {
        Sheet sheet = workbook.getSheet(MAIN_LIST_NAME);
        if (sheet == null) {
            throw new IllegalStateException("Не найден лист " + MAIN_LIST_NAME);
        }
        inputParameters.forEach(inputParameter -> setValueByCode(sheet, inputParameter.fullCode(), inputParameter.inValue(), inputParameter.dictValue()));
    }

    private void setValueByCode(Sheet sheet, String code, Object value, String dictValue) {
        Row row = getRowByCode(sheet, code)
                .orElseThrow(() -> new RuntimeException("Параметр с кодом '" + code + "' не найден"));

        Map<String, Integer> headerColumnsMap = getHeaderColumnsMap(sheet);
        Cell cell = row.getCell(headerColumnsMap.get(COLUMN_PARAM_VALUE));

        String valueType = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_TYPE)));
        if (value != null) {
            setValue(cell, value, valueType);
        }
        if (dictValue != null) {
            row.getCell(headerColumnsMap.get(COLUMN_PARAM_DICT_CODE)).setCellValue(dictValue);
        }
    }

    private List<OutputParameter> readOutputParametersByCodes(Sheet sheet, FormulaEvaluator evaluator, List<String> outputCodes) throws IOException {
        return readOutputParameters(sheet, evaluator)
                .filter(outputParameter -> outputCodes.contains(outputParameter.fullCode()))
                .toList();
    }

    private List<IOParameter> readIOParameters(Sheet sheet, FormulaEvaluator evaluator) throws IOException {
        return readIOParametersStream(sheet, evaluator)
                .filter(this::isNonEmptyValuesParameter)
                .toList();
    }

    private boolean isNonEmptyValuesParameter(IOParameter ioParameter) {
        if (!showEmptyValueParameters) {
            return nonEmpty(ioParameter.inValue()) ||
                    nonEmpty(ioParameter.calcValue()) ||
                    nonEmpty(ioParameter.finalValue());
        }
        return true;
    }

    private boolean nonEmpty(Object value) {
        if (value == null)
            return false;
        if (value instanceof String) {
            return !((String) value).isEmpty();
        }
        return true;
    }

    private Object getOutputValueByCode(Sheet sheet, String code) {
        Row row = getRowByCode(sheet, code)
                .orElseThrow(() -> new RuntimeException("Параметр с кодом '" + code + "' не найден"));
        Map<String, Integer> headerColumnsMap = getHeaderColumnsMap(sheet);
        String valueType = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_TYPE)));
        Cell cell = row.getCell(headerColumnsMap.get(COLUMN_PARAM_OUTPUT));
        return getValue(cell, valueType);
    }

    private Optional<Row> getRowByCode(Sheet sheet, String code) {
        Map<String, Integer> headerColumnsMap = getHeaderColumnsMap(sheet);
        return readParameterRows(sheet)
                .map(row -> row.getCell(headerColumnsMap.get(COLUMN_PARAM_CODE)))
                .filter(cell -> Objects.equals(getStringCell(cell), code))
                .map(Cell::getRow)
                .findFirst();
    }

    private Stream<OutputParameter> readOutputParameters(Sheet sheet, FormulaEvaluator evaluator) {
        Map<String, Integer> headerColumnsMap = getHeaderColumnsMap(sheet);
        return readParameterRows(sheet)
                .map(row -> {
                    String name = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_NAME)));
                    String fullCode = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_CODE)));
                    if ((name == null || name.isBlank()) && (fullCode == null || fullCode.isBlank())) {
                        return null;
                    }

                    String valueType = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_TYPE)));
                    if (valueType == null || valueType.isBlank()) {
                        return null;
                    }
                    Object outputValue = getValue(row.getCell(headerColumnsMap.get(COLUMN_PARAM_OUTPUT)), valueType);
                    Object finalOutputValue = evaluateValue(evaluator,
                            row.getCell(headerColumnsMap.get(COLUMN_PARAM_FINAL_OUTPUT)),
                            valueType);

                    return new OutputParameter(
                            name == null ? "" : name,
                            fullCode == null ? "" : fullCode,
                            outputValue == null ? "" : outputValue,
                            finalOutputValue == null ? "" : finalOutputValue
                    );
                })
                .filter(Objects::nonNull);
    }

    private Stream<IOParameter> readIOParametersStream(Sheet sheet, FormulaEvaluator evaluator) {
        Map<String, Integer> headerColumnsMap = getHeaderColumnsMap(sheet);
        return readParameterRows(sheet)
                .map(row -> {
                    String name = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_NAME)));
                    String fullCode = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_CODE)));
                    if ((name == null || name.isBlank()) && (fullCode == null || fullCode.isBlank())) {
                        return null;
                    }

                    String valueType = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_TYPE)));
                    if (valueType == null || valueType.isBlank()) {
                        return null;
                    }
                    Object inValue = getValue(row.getCell(headerColumnsMap.get(COLUMN_PARAM_VALUE)), valueType);
                    String dictValue = getStringCell(row.getCell(headerColumnsMap.get(COLUMN_PARAM_DICT_CODE)));

                    Object calcValue = getValue(row.getCell(headerColumnsMap.get(COLUMN_PARAM_OUTPUT)), valueType);
                    Object finalOutputValue = evaluateValue(evaluator,
                            row.getCell(headerColumnsMap.get(COLUMN_PARAM_FINAL_OUTPUT)),
                            valueType);

                    return new IOParameter(
                            name == null ? "" : name,
                            fullCode == null ? "" : fullCode,
                            dictValue == null ? "" : dictValue,
                            inValue == null ? "" : inValue,
                            calcValue == null ? "" : calcValue,
                            finalOutputValue == null ? "" : finalOutputValue
                    );
                })
                .filter(Objects::nonNull);
    }

    private Stream<Row> readParameterRows(Sheet sheet) {
        return IntStream.range(START_PARAMS_ROW, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull);
    }

    private Map<String, Integer> getHeaderColumnsMap(Sheet sheet) {
        Row headerRow = sheet.getRow(HEADER_ROW);
        if (headerRow == null) {
            throw new IllegalStateException("Не найдены заголовки");
        }

        int nameCol = -1;
        int codeCol = -1;
        int valueCol = -1;
        int dictValCol = -1;
        int outputCol = -1;
        int finalOutputCol = -1;
        int typeCol = -1;

        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            Cell cell = headerRow.getCell(c);
            if (cell == null) continue;
            String text = cell.toString().trim();
            if (text.equalsIgnoreCase(COLUMN_PARAM_NAME)) {
                nameCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_CODE)) {
                codeCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_VALUE)) {
                valueCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_DICT_CODE)) {
                dictValCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_OUTPUT)) {
                outputCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_FINAL_OUTPUT)) {
                finalOutputCol = c;
            } else if (text.equalsIgnoreCase(COLUMN_PARAM_TYPE)) {
                typeCol = c;
            }
        }

        if (nameCol < 0 ||
                codeCol < 0 ||
                valueCol < 0 ||
                dictValCol < 0 ||
                typeCol < 0 ||
                outputCol < 0 ||
                finalOutputCol < 0) {
            throw new IllegalStateException("Файл не соответствует ожидаемому формату. " +
                    "Не найдены обязательные колонки");
        }

        return Map.of(COLUMN_PARAM_NAME, nameCol,
                COLUMN_PARAM_CODE, codeCol,
                COLUMN_PARAM_VALUE, valueCol,
                COLUMN_PARAM_DICT_CODE, dictValCol,
                COLUMN_PARAM_OUTPUT, outputCol,
                COLUMN_PARAM_FINAL_OUTPUT, finalOutputCol,
                COLUMN_PARAM_TYPE, typeCol);
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

    private Object getValue(Cell cell, String type) {
        if (cell == null) return null;

        // Вычисляем формулу (если есть)
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            try {
                cellType = cell.getCachedFormulaResultType();
            } catch (Exception e) {
                // Если формула с ошибкой (#Н/Д, #REF и т.п.)
                return null; // или дефолт
            }
        }

        return switch (type) {
            case "Строка" -> {
                if (cellType == CellType.STRING) yield cell.getStringCellValue();
                yield safeToString(cell); // fallback
            }
            case "Целое", "Вещественный" -> {
                if (cellType == CellType.NUMERIC) yield cell.getNumericCellValue();
                else yield null; // например, пропускаем если ошибка
            }
            case "Дата" -> {
                if (cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
                    yield cell.getDateCellValue();
                else yield null;
            }
            case "Логический" -> {
                if (cellType == CellType.BOOLEAN) yield cell.getBooleanCellValue();
                else yield null;
            }
            default -> null;
        };
    }

    private String safeToString(Cell cell) {
        try {
            return cell.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private Object evaluateValue(FormulaEvaluator evaluator, Cell cell, String type) {
        if (cell == null) return null;

        CellValue cellValue = evaluator.evaluate(cell);

        return switch (type) {
            case "Строка" -> cellValue.getStringValue();
            case "Целое", "Вещественный" -> cellValue.getNumberValue();
            case "Дата" -> {
                double numericValue = cellValue.getNumberValue();
                yield DateUtil.getJavaDate(numericValue);
            }
            case "Логический" -> cellValue.getBooleanValue();
            default -> null;
        };
    }

    private void setValue(Cell cell, Object value, String valueType) {
        if (value != null && valueType != null) {
            switch (valueType) {
                case "Строка" -> cell.setCellValue(value.toString());

                case "Целое" -> {
                    if (value instanceof Number num) {
                        cell.setCellValue(num.longValue());
                    } else {
                        cell.setCellValue(Long.parseLong(value.toString()));
                    }
                }

                case "Вещественный" -> {
                    if (value instanceof Number num) {
                        cell.setCellValue(num.doubleValue());
                    } else {
                        cell.setCellValue(Double.parseDouble(value.toString()));
                    }
                }

                case "Дата" -> {
                    if (value instanceof Date date) {
                        cell.setCellValue(date);
                    } else {
                        throw new IllegalArgumentException("Ожидалась дата, а пришло: " + value);
                    }
                }

                case "Логический" -> {
                    if (value instanceof Boolean bool) {
                        cell.setCellValue(bool);
                    } else {
                        cell.setCellValue(Boolean.parseBoolean(value.toString()));
                    }
                }

                default -> throw new IllegalArgumentException("Неизвестный тип: " + valueType);
            }
        } else {
            throw new IllegalArgumentException("Arguments value and value type can't be null");
        }
    }

}
