package com.ctecx.brs.tenant.countries;


import com.ctecx.brs.tenant.exception.EntityNotFoundException;
import com.ctecx.brs.tenant.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    @Transactional
    public CountryDTO save(CountryDTO dto) {
        if (countryRepository.existsByName(dto.getName())) {
            throw new ValidationException("Country with name " + dto.getName() + " already exists");
        }
        if (countryRepository.existsByAbbreviation(dto.getAbbreviation())) {
            throw new ValidationException("Country with abbreviation " + dto.getAbbreviation() + " already exists");
        }
        Country country = Country.builder()
                .name(dto.getName())
                .abbreviation(dto.getAbbreviation())
                .capitalCity(dto.getCapitalCity())
                .continent(dto.getContinent())
                .status(dto.isStatus())
                .build();
        country = countryRepository.save(country);
        dto.setId(country.getId());
        return dto;
    }

    @Transactional
    public CountryDTO update(Long id, CountryDTO dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country with ID " + id + " not found"));
        if (!country.getName().equals(dto.getName()) && countryRepository.existsByName(dto.getName())) {
            throw new ValidationException("Country with name " + dto.getName() + " already exists");
        }
        if (!country.getAbbreviation().equals(dto.getAbbreviation()) && countryRepository.existsByAbbreviation(dto.getAbbreviation())) {
            throw new ValidationException("Country with abbreviation " + dto.getAbbreviation() + " already exists");
        }
        country.setName(dto.getName());
        country.setAbbreviation(dto.getAbbreviation());
        country.setCapitalCity(dto.getCapitalCity());
        country.setContinent(dto.getContinent());
        country.setStatus(dto.isStatus());
        countryRepository.save(country);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new EntityNotFoundException("Country with ID " + id + " not found");
        }
        countryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CountryDTO findById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country with ID " + id + " not found"));
        return new CountryDTO(
                country.getId(),
                country.getName(),
                country.getAbbreviation(),
                country.getCapitalCity(),
                country.getContinent(),
                country.isStatus()
        );
    }

    @Transactional(readOnly = true)
    public List<CountryDTO> findAll() {
        return countryRepository.findAll().stream()
                .map(country -> new CountryDTO(
                        country.getId(),
                        country.getName(),
                        country.getAbbreviation(),
                        country.getCapitalCity(),
                        country.getContinent(),
                        country.isStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CountryDTO> searchCountries(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Country> countryPage = countryRepository.findByNameContainingIgnoreCase(searchTerm == null ? "" : searchTerm, pageable);
        return countryPage.map(country -> new CountryDTO(
                country.getId(),
                country.getName(),
                country.getAbbreviation(),
                country.getCapitalCity(),
                country.getContinent(),
                country.isStatus()
        ));
    }

    @Transactional(readOnly = true)
    public Page<CountryDTO> searchActiveCountries(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Country> countryPage = countryRepository.findActiveByNameContainingIgnoreCase(searchTerm == null ? "" : searchTerm, pageable);
        return countryPage.map(country -> new CountryDTO(
                country.getId(),
                country.getName(),
                country.getAbbreviation(),
                country.getCapitalCity(),
                country.getContinent(),
                country.isStatus()
        ));
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getCountryStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", countryRepository.countAllCountries());
        stats.put("active", countryRepository.countActiveCountries());
        stats.put("inactive", countryRepository.countInactiveCountries());
        return stats;
    }
}