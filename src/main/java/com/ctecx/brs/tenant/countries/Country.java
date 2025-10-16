package com.ctecx.brs.tenant.countries;


import com.ctecx.brs.tenant.util.AuditableBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country extends AuditableBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country name is required")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Abbreviation is required")
    @Size(min = 2, max = 3, message = "Abbreviation must be 2 or 3 characters")
    @Column(nullable = false)
    private String abbreviation;

    @NotBlank(message = "Capital city is required")
    @Size(min = 2, max = 100, message = "Capital city must be between 2 and 100 characters")
    @Column(nullable = false)
    private String capitalCity;

    @NotBlank(message = "Continent is required")
    @Size(min = 2, max = 50, message = "Continent must be between 2 and 50 characters")
    @Column(nullable = false)
    private String continent;

    @Column(nullable = false)
    private boolean status;
}