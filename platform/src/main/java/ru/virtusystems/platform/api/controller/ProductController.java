package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.virtusystems.platform.dto.ReadProductDto;
import ru.virtusystems.platform.service.office.OfficeProductService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final OfficeProductService officeProductService;

    @GetMapping
    public ResponseEntity<List<ReadProductDto>> getAllProducts() {
        return ResponseEntity.ok(officeProductService.getAllProducts());
    }
}
