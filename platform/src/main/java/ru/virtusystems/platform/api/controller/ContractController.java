package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.dto.ReadProductDto;
import ru.virtusystems.platform.service.office.OfficeContractService;
import ru.virtusystems.platform.service.office.OfficeProductService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final OfficeContractService officeContractService;

    @GetMapping
    public ResponseEntity<List<ReadContractDto>> getContracts() {
        return ResponseEntity.ok(officeContractService.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadContractDto> getContracts(@PathVariable Long id) {
        return ResponseEntity.ok(officeContractService.getContractById(id));
    }
}
