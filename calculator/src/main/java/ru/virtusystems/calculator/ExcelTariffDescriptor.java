package ru.virtusystems.calculator;

import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.virtusystems.calculator.mapper.ContractParametersMapper;
import ru.virtusystems.calculator.port.AccessibleTypesCollector;
import ru.virtusystems.calculator.port.TariffEvaluator;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.port.TariffDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Getter
public class ExcelTariffDescriptor implements TariffDescriptor {
    private final Path pathToTariffFile;
    private final byte[] file;
    private final TariffEvaluator tariffEvaluator;
    private final AccessibleTypesCollector accessibleTypesCollector;
    private final ContractParametersMapper contractParametersMapper;
    private final Map<String, Map<String, String>> dictionary;

    public ExcelTariffDescriptor(Path pathToTariffFile) {
        this.pathToTariffFile = pathToTariffFile;
        this.file = getFile();
        this.accessibleTypesCollector = new ExcelAccessibleTypesCollector();
        this.dictionary = getDictionary();
        this.contractParametersMapper = new ContractParametersMapper(dictionary);
        this.tariffEvaluator = new ExcelTariffEvaluator(contractParametersMapper);
    }

    @Override
    public Map<String, Map<String, String>> getAccessibleTypesMap(TariffEvaluationState inputState) {
        try (Workbook workbook = newWorkbook()) {
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            tariffEvaluator.setParams(workbook, contractParametersMapper.mapToInputParameters(
                    inputState.getParameters()));
            return accessibleTypesCollector.getAccessibleTypes(workbook, formulaEvaluator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TariffEvaluationState evaluateState(TariffEvaluationState inputState) {
        try (Workbook workbook = newWorkbook()) {
            return tariffEvaluator.evaluateState(workbook, inputState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> getAccessibleTypesByCode(String code) {
        try (Workbook workbook = newWorkbook()) {
            return accessibleTypesCollector.getAccessibleTypesByCode(workbook, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Map<String, Map<String, String>> getDictionary() {
        try (Workbook workbook = newWorkbook()) {
            return accessibleTypesCollector.getAllTypes(workbook);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Workbook newWorkbook() throws IOException {
        String filename = pathToTariffFile.getFileName().toString();
        if (filename.toLowerCase().endsWith("xlsx")) {
            return new XSSFWorkbook(new ByteArrayInputStream(file));
        } else if (filename.toLowerCase().endsWith("xls")) {
            return new HSSFWorkbook(new ByteArrayInputStream(file));
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + filename);
        }
    }

    private byte[] getFile() {
        try (InputStream fis = Files.newInputStream(pathToTariffFile)) {
            return fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
