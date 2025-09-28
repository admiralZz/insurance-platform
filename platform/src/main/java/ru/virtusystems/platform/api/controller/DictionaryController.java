package ru.virtusystems.platform.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.virtusystems.platform.dto.CountryDto;
import ru.virtusystems.platform.service.office.OfficeDictionaryService;

import java.util.List;

// TODO настроить корс
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final OfficeDictionaryService officeDictionaryService;

    @GetMapping("countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(officeDictionaryService.getAllCountries());
    }
}
