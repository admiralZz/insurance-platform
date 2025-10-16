package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.virtusystems.platform.dto.PageResponse;
import ru.virtusystems.platform.dto.ReadAccessibleTypesDto;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.dto.filter.ContractFilter;
import ru.virtusystems.platform.service.office.OfficeDispatcherService;

// TODO настроить корс
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeDispatcherService officeDispatcherService;

    @GetMapping
    public PageResponse<ReadContractDto> getContracts(ContractFilter filter, Pageable pageable) {
        return officeDispatcherService.getContractPageResponse(filter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadContractDto> getContract(@PathVariable Long id) {
        return ResponseEntity.ok(officeDispatcherService.getContractById(id));
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<ReadAccessibleTypesDto> getAccessibleTypes(@PathVariable Long id) {
        return ResponseEntity.ok(officeDispatcherService.getAccessibleTypesById(id));
    }

    @GetMapping("/types")
    public ResponseEntity<ReadAccessibleTypesDto> getAccessibleTypes(@RequestParam String product) {
        return ResponseEntity.ok(officeDispatcherService.getAccessibleTypesByProduct(product));
    }

    @GetMapping("/number")
    public ResponseEntity<ReadContractDto> getContractByNumber(@RequestParam String number) {
        return ResponseEntity.ok(officeDispatcherService.getContractByNumber(number));
    }
}
