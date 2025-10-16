package com.ctecx.brs.tenant.company;



import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company createCompany(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setAddress(companyDTO.getAddress());
        company.setCity(companyDTO.getCity());
        company.setState(companyDTO.getState());
        company.setZipCode(companyDTO.getZipCode());
        company.setCountry(companyDTO.getCountry());
        company.setTaxId(companyDTO.getTaxId());
        company.setWebsite(companyDTO.getWebsite());
        return companyRepository.save(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + id + " not found"));
    }

    public Company updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = getCompanyById(id);
        company.setCompanyName(companyDTO.getCompanyName());
        company.setAddress(companyDTO.getAddress());
        company.setCity(companyDTO.getCity());
        company.setState(companyDTO.getState());
        company.setZipCode(companyDTO.getZipCode());
        company.setCountry(companyDTO.getCountry());
        company.setTaxId(companyDTO.getTaxId());
        company.setWebsite(companyDTO.getWebsite());
        return companyRepository.save(company);
    }
}