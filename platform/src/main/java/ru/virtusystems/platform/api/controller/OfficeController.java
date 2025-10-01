package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.virtusystems.platform.dto.ReadAccessibleTypesDto;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.service.office.OfficeDispatcher;

import java.util.List;

// TODO настроить корс
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeDispatcher officeDispatcher;

    @GetMapping
    public ResponseEntity<List<ReadContractDto>> getContracts() {
        return ResponseEntity.ok(officeDispatcher.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadContractDto> getContracts(@PathVariable Long id) {
        return ResponseEntity.ok(officeDispatcher.getContractById(id));
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<ReadAccessibleTypesDto> getAccessibleTypes(@PathVariable Long id) {
        return ResponseEntity.ok(officeDispatcher.getAccessibleTypesById(id));
    }
}
