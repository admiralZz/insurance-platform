package ru.virtusystems.platform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.virtusystems.platform.conf.TestZachitaDohoda20ProductConfiguration;

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

    @Test
    public void test() throws Exception {
        String json = """
                {
                  "product": "Защита дохода 2.0",
                  "calc": {
                    "program": "Стандартный",
                    "period": "90 дней"
                  }
                }
                """;

        mockMvc.perform(
                        post("/api/partner/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());
    }
}
