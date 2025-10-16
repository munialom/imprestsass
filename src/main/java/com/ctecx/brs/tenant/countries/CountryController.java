package com.ctecx.brs.tenant.countries;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<CountryDTO> create(@Valid @RequestBody CountryDTO dto) {
        return new ResponseEntity<>(countryService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDTO> update(@PathVariable Long id, @Valid @RequestBody CountryDTO dto) {
        return ResponseEntity.ok(countryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAll() {
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CountryDTO>> search(
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(countryService.searchCountries(searchTerm, page, size));
    }

    @GetMapping("/search/active")
    public ResponseEntity<Page<CountryDTO>> searchActive(
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(countryService.searchActiveCountries(searchTerm, page, size));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getCountryStats() {
        return ResponseEntity.ok(countryService.getCountryStats());
    }
}