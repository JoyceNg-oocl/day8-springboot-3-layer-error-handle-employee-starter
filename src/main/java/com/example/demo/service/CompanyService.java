package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void empty() {
        companyRepository.empty();
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        return companyRepository.getCompanies(page, size);
    }

    public Company createCompany(Company company) {
        return companyRepository.createCompany(company);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company found = companyRepository.updateCompany(id, updatedCompany);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return found;
    }

    public Company getCompanyById(int id) {
        Company company = companyRepository.getCompanyById(id);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company;
    }

    public void deleteCompany(int id) {
        Company found = companyRepository.getCompanyById(id);
        if (found != null) {
            companyRepository.deleteCompany(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
    }
}
