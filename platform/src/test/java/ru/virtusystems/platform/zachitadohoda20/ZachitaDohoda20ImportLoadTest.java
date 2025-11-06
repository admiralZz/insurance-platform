package ru.virtusystems.platform.zachitadohoda20;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.virtusystems.platform.ConcurrentIntegrationTest;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.conf.TestZachitaDohoda20ProductConfiguration;
import ru.virtusystems.platform.dto.ReadContractDto;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Slf4j
@Import(TestZachitaDohoda20ProductConfiguration.class)
@RequiredArgsConstructor
public class ZachitaDohoda20ImportLoadTest extends ConcurrentIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void loadTest_ManyParallelImports_ShouldMatchExpectedPremiumsAndInsuranceSums() throws Exception {
        Map<String, Expected> expectations = Map.of(
                "Базовый", new Expected(BigDecimal.valueOf(3000.0), BigDecimal.valueOf(150000.0)),
                "Стандартный", new Expected(BigDecimal.valueOf(1000.0), BigDecimal.valueOf(50000.0)),
                "Премиум", new Expected(BigDecimal.valueOf(5000.0), BigDecimal.valueOf(300000.0))
        );

        List<String> programs = new ArrayList<>();
        // 60 requests total: 20 for each program
        for (int i = 0; i < 20; i++) {
            programs.add("Базовый");
            programs.add("Стандартный");
            programs.add("Премиум");
        }

        ExecutorService executor = Executors.newFixedThreadPool(12);
        try {
            List<CompletableFuture<Result>> futures = programs.stream()
                    .map(program -> CompletableFuture.supplyAsync(() -> doImport(program), executor))
                    .toList();

            List<Result> results = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            for (Result r : results) {
                Expected expected = expectations.get(r.program);
                Assertions.assertNotNull(expected, "No expectations for program: " + r.program);
                Assertions.assertEquals(expected.premium, r.response.getContract().getPremium(),
                        () -> "Wrong premium for program " + r.program);
                Assertions.assertEquals(expected.insuredSum, r.response.getContract().getInsuredSum(),
                        () -> "Wrong insured sum for program " + r.program);
            }
            checkCalcIdOrder(results);
            checkContractNumberOrder(results);
        } finally {
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void checkCalcIdOrder(List<Result> results) {
        // Проверяем что все calcId идут по порядку, нету пропущенных номеров
        Set<Integer> foundNumbers = results.stream()
                .map(Result::response)
                .map(ContractResponse::getContract)
                .map(ReadContractDto::getCalcId)
                .map(calcId -> Integer.parseInt(calcId.split("_")[1]))
                .collect(Collectors.toSet());

        List<Integer> missingNumbers = IntStream.rangeClosed(1, 60)
                .filter(n -> !foundNumbers.contains(n))
                .boxed()
                .toList();
        Assertions.assertTrue(missingNumbers.isEmpty(), "There are missing calcIds. " +
                "Missing numbers: " + missingNumbers);
    }

    private void checkContractNumberOrder(List<Result> results) {
        // Проверяем что все номера идут по порядку, нету пропущенных номеров
        Set<Integer> foundNumbers = results.stream()
                .map(Result::response)
                .map(ContractResponse::getContract)
                .map(ReadContractDto::getNumber)
                .map(number -> Integer.parseInt(number.replace("ФРФЗДРНК", "")))
                .collect(Collectors.toSet());

        List<Integer> missingNumbers = IntStream.rangeClosed(1, 60)
                .filter(n -> !foundNumbers.contains(n))
                .boxed()
                .toList();
        Assertions.assertTrue(missingNumbers.isEmpty(), "There are missing numbers. " +
                "Missing numbers: " + missingNumbers);
    }

    private Result doImport(String program) {
        String request = """
                {
                  "product": "Защита дохода 2.0",
                  "calc": {
                    "program": "%s",
                    "period": "181 день"
                  },
                  "insured": {
                    "lastName": "Пупкин",
                    "firstName": "Василий",
                    "birthDate": "2001-08-30",
                    "citizenship": "Россия",
                    "gender": "MALE"
                  }
                }
                """.formatted(program);
        try {
            MvcResult mvcResult = mockMvc.perform(
                            post("/api/partner/import")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request.getBytes(StandardCharsets.UTF_8))
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            ContractResponse contractResponse = objectMapper
                    .readValue(mvcResult.getResponse().getContentAsString(), ContractResponse.class);
            return new Result(program, contractResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private record Expected(BigDecimal premium, BigDecimal insuredSum) {}

    private record Result(String program, ContractResponse response) {}
}


