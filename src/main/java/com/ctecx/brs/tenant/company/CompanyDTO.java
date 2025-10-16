package com.ctecx.brs.tenant.company;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyDTO {
    @NotBlank(message = "Company name cannot be blank")
    @Size(max = 100)
    private String companyName;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255)
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100)
    private String state;

    @NotBlank(message = "ZIP code cannot be blank")
    @Size(max = 20)
    private String zipCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100)
    private String country;

    @NotBlank(message = "Tax ID cannot be blank")
    @Size(max = 50)
    private String taxId;

    @Size(max = 255)
    private String website;
}
