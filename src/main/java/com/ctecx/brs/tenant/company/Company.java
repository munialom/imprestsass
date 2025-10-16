// Company.java
package com.ctecx.brs.tenant.company;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @NotBlank(message = "Company name cannot be blank")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    @Column(name = "state", nullable = false)
    private String state;

    @NotBlank(message = "ZIP code cannot be blank")
    @Size(max = 20, message = "ZIP code cannot exceed 20 characters")
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "Tax ID cannot be blank")
    @Size(max = 50, message = "Tax ID cannot exceed 50 characters")
    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Size(max = 255, message = "Website cannot exceed 255 characters")
    @Column(name = "website")
    private String website;
}
