package ru.virtusystems.platform.dispatcher;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.virtusystems.api.request.CalculateRequest;
import ru.virtusystems.api.request.IssueRequest;
import ru.virtusystems.api.request.SaveRequest;
import ru.virtusystems.api.request.UpdateRequest;
import ru.virtusystems.api.response.ContractResponse;

@Component
@RequiredArgsConstructor
public class ProductDispatcher {

    private final Map<String, ProductFacade<? extends CalculateRequest>> productMap;

    @PostConstruct
    private void initMap() {
        // Можно собрать автоматически через Spring:
        // productMap.put("DMS", dmsProductFacade);
        // productMap.put("RNS", rnsProductFacade);
    }

    public ContractResponse dispatch(String productCode, CalculateRequest request) {
        ProductFacade facade = productMap.get(productCode);
        if (facade == null) {
            throw new IllegalArgumentException("Unsupported product code: " + productCode);
        }
        return facade.calculate(request);
    }
}
