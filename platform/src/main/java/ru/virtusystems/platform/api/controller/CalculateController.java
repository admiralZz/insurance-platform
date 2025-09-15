package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.virtusystems.api.request.CalculateRequest;
import ru.virtusystems.api.response.CalculateResponse;
import ru.virtusystems.service.dms.DmsCalculationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalculateController {

    private final DmsCalculationService dmsCalculationService;

    @PostMapping("calculate")
    public ResponseEntity<CalculateResponse> calculate(@RequestBody CalculateRequest calculateRequest) {
        return ResponseEntity.ok(dmsCalculationService.calculate(calculateRequest));
    }
}
