package com.example.demo.service;

import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.mapper.CompanyMapper;
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
    private final CompanyMapper companyMapper;

    public CompanyService(ICompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public List<CompanyResponse> getCompanies(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyMapper.toResponse(companyRepository.findAll());
        }
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return companyMapper.toResponse(companyRepository.findAll(pageable).stream().toList());
    }

    public CompanyResponse createCompany(Company company) {
        return companyMapper.toResponse(companyRepository.save(company));
    }

    public CompanyResponse updateCompany(int id, Company updatedCompany) {
        Company found = companyRepository.getCompanyById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        updatedCompany.setId(id);
        return companyMapper.toResponse(companyRepository.save(updatedCompany));
    }

    public CompanyResponse getCompanyById(int id) {
        Company company = companyRepository.getCompanyById(id);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return companyMapper.toResponse(company);
    }

    public void deleteCompany(int id) {
        Company found = companyRepository.getCompanyById(id);
        if (found != null) {
            companyRepository.delete(found);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
    }
}
