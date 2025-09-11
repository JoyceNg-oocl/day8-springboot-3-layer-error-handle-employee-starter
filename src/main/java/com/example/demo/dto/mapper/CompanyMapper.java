package com.example.demo.dto.mapper;

import com.example.demo.dto.CompanyResponse;
import com.example.demo.entity.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    private final EmployeeMapper employeeMapper;

    public CompanyMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public CompanyResponse toResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        BeanUtils.copyProperties(company, response, "employees");
        if (company.getEmployees() != null) {
            response.setEmployees(employeeMapper.toResponse(company.getEmployees()));
        }
        return response;
    }

    public List<CompanyResponse> toResponse(List<Company> companies) {
        return companies.stream().map(this::toResponse).toList();
    }
}

