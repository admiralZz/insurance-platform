package ru.virtusystems.platform.zachitadohoda20;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.virtusystems.platform.IntegrationTest;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.conf.TestZachitaDohoda20ProductConfiguration;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // для получения http-клиента в виде MockMvc
@Slf4j
@Sql({
        "classpath:sql/insured.sql",
})
@Import(TestZachitaDohoda20ProductConfiguration.class)
@RequiredArgsConstructor
public class ZachitaDohoda20Test extends IntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void testProgramBase() throws Exception {
        String request = """
                {
                  "product": "Защита дохода 2.0",
                  "calc": {
                    "program": "Базовый",
                    "period": "181 день"
                  }
                }
                """;

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/partner/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andReturn();

        ContractResponse contractResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ContractResponse.class);
        Assertions.assertEquals(contractResponse.getContract().getPremium(), BigDecimal.valueOf(3000.0));
        Assertions.assertEquals(contractResponse.getContract().getInsuredSum(), BigDecimal.valueOf(150000.0));
    }

    @Test
    public void testProgramStandard() throws Exception {
        String request = """
                {
                  "product": "Защита дохода 2.0",
                  "calc": {
                    "program": "Стандартный",
                    "period": "181 день"
                  }
                }
                """;

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/partner/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andReturn();

        ContractResponse contractResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ContractResponse.class);
        Assertions.assertEquals(contractResponse.getContract().getPremium(), BigDecimal.valueOf(1000.0));
        Assertions.assertEquals(contractResponse.getContract().getInsuredSum(), BigDecimal.valueOf(50000.0));
    }

    @Test
    public void testProgramPremium() throws Exception {
        String request = """
                {
                  "product": "Защита дохода 2.0",
                  "calc": {
                    "program": "Премиум",
                    "period": "181 день"
                  }
                }
                """;

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/partner/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andReturn();

        ContractResponse contractResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ContractResponse.class);
        Assertions.assertEquals(contractResponse.getContract().getPremium(), BigDecimal.valueOf(5000.0));
        Assertions.assertEquals(contractResponse.getContract().getInsuredSum(), BigDecimal.valueOf(300000.0));
    }
}
