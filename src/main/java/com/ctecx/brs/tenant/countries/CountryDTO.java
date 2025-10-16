package com.ctecx.brs.tenant.countries;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Long id;

    @NotBlank(message = "Country name is required")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Abbreviation is required")
    @Size(min = 2, max = 3, message = "Abbreviation must be 2 or 3 characters")
    private String abbreviation;

    @NotBlank(message = "Capital city is required")
    @Size(min = 2, max = 100, message = "Capital city must be between 2 and 100 characters")
    private String capitalCity;

    @NotBlank(message = "Continent is required")
    @Size(min = 2, max = 50, message = "Continent must be between 2 and 50 characters")
    private String continent;

    private boolean status;
}