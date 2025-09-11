package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {
    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyRepository.findAll();
        }
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return companyRepository.findAll(pageable).stream().toList();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company found = getCompanyById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        updatedCompany.setId(id);
        return companyRepository.save(updatedCompany);
    }

    public Company getCompanyById(int id) {
        Company company = companyRepository.getCompanyById(id);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company;
    }

    public void deleteCompany(int id) {
        Company found = getCompanyById(id);
        if (found != null) {
            companyRepository.delete(found);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
    }
}
