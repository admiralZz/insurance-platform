package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.virtusystems.api.request.CalculateRequest;
import ru.virtusystems.api.request.IssueRequest;
import ru.virtusystems.api.request.SaveRequest;
import ru.virtusystems.api.request.UpdateRequest;
import ru.virtusystems.api.response.ContractResponse;
import ru.virtusystems.service.dms.DmsContractService;

@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final DmsContractService dmsContractService;

    @PostMapping("calculate")
    public ResponseEntity<ContractResponse> calculate(@Validated @RequestBody CalculateRequest calculateRequest) {
        return ResponseEntity.ok(dmsContractService.calculate(calculateRequest));
    }

    @PostMapping("import")
    public ResponseEntity<ContractResponse> save(@Validated @RequestBody SaveRequest saveRequest) {
        return ResponseEntity.ok(dmsContractService.save(saveRequest));
    }

    @PutMapping("update")
    public ResponseEntity<ContractResponse> update(@Validated @RequestBody UpdateRequest updateRequest) {
        return ResponseEntity.ok(dmsContractService.update(updateRequest));
    }

    @PutMapping("issue")
    public ResponseEntity<ContractResponse> issue(@Validated @RequestBody IssueRequest issueRequest) {
        return ResponseEntity.ok(dmsContractService.issue(issueRequest));
    }
}
