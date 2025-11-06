package ru.virtusystems.platform.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.virtusystems.domain.model.evaluator.TariffEvaluationState;
import ru.virtusystems.domain.model.types.ContractParameter;
import ru.virtusystems.domain.port.TariffDescriptor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Primary
public class TariffWebClient implements TariffDescriptor {

    private final RestClient restClient;

    public TariffWebClient(RestClient.Builder builder,
                           @Value("${app.tariff-client.url}") String tariffUrl) {
        this.restClient = builder
                .baseUrl(tariffUrl)
                .build();
    }

    @Override
    public Map<String, Map<String, String>> getAccessibleTypesMap(TariffEvaluationState inputState) {
        Map<String, Map<String, String>> response = restClient
                .post()
                .uri("/api/tariff/types")
                .contentType(MediaType.APPLICATION_JSON)
                .body(inputState)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return Objects.requireNonNullElse(response, Map.of());
    }

    @Override
    public TariffEvaluationState evaluateState(TariffEvaluationState inputState) {
        return Optional.ofNullable(restClient
                .post()
                .uri("/api/tariff/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(inputState)
                .retrieve()
                .body(ResultTariffStateDto.class))
                .map(ResultMapper::map)
                .orElse(null);
    }

    @Override
    public Map<String, String> getAccessibleTypesByCode(String code) {
        Map<String, String> response = restClient
                .get()
                .uri("/api/tariff/code/{code}", code)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, String>>() {})
                ;
        return Objects.requireNonNullElse(response, Map.of());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class ResultTariffStateDto {
        private List<ContractParameter> parameters;
    }

    private static class ResultMapper {
        static TariffEvaluationState map(ResultTariffStateDto dto) {
            return TariffEvaluationState.builder()
                    .parameters(dto.getParameters())
                    .build();
        }
    }
}
