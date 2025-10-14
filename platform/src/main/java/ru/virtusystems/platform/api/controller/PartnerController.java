package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.virtusystems.platform.api.request.ProductCalculateRequest;
import ru.virtusystems.platform.api.request.ProductIssueRequest;
import ru.virtusystems.platform.api.request.ProductSaveRequest;
import ru.virtusystems.platform.api.request.ProductUpdateRequest;
import ru.virtusystems.platform.api.response.ContractResponse;
import ru.virtusystems.platform.service.ProductDispatcher;

// TODO настроить корс
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final ProductDispatcher productDispatcher;

    @PostMapping("calculate")
    public ResponseEntity<ContractResponse> calculate(@Validated @RequestBody ProductCalculateRequest productCalculateRequest) {
        return ResponseEntity.ok(productDispatcher.calculate(productCalculateRequest));
    }

    @PostMapping("import")
    public ResponseEntity<ContractResponse> save(@Validated @RequestBody ProductSaveRequest productSaveRequest) {
        return ResponseEntity.ok(productDispatcher.save(productSaveRequest));
    }

    @PutMapping("update")
    public ResponseEntity<ContractResponse> update(@Validated @RequestBody ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.ok(productDispatcher.update(productUpdateRequest));
    }

    @PutMapping("issue")
    public ResponseEntity<ContractResponse> issue(@Validated @RequestBody ProductIssueRequest productIssueRequest) throws Exception {
        return ResponseEntity.ok(productDispatcher.issue(productIssueRequest));
    }
}
