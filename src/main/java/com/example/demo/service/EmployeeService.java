package com.example.demo.service;

import com.example.demo.InactiveStatusException;
import com.example.demo.InvalidAgeEmployeeException;
import com.example.demo.InvalidSalaryEmployeeException;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.entity.Employee;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(IEmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeResponse> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeMapper.toResponse(employeeRepository.findAll());
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeMapper.toResponse(employeeRepository.findAll(pageable).stream().toList());
            }
        } else {
            if (page == null || size == null) {
                return employeeMapper.toResponse(employeeRepository.findEmployeesByGender(gender));
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeMapper.toResponse(employeeRepository.findEmployeesByGender(gender, pageable));
            }
        }
    }

    public EmployeeResponse getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeMapper.toResponse(employee.get());
    }

    public EmployeeResponse createEmployee(Employee employee) {
        Integer age = employee.getAge();
        if (age == null) {
            throw new InvalidAgeEmployeeException("Employee age is null!");
        }
        if (age < 18 || age > 65) {
            throw new InvalidAgeEmployeeException("Employee age less than 18 or greater than 65!");
        }
        if (age >= 30 && employee.getSalary() < 20000) {
            throw new InvalidSalaryEmployeeException("Employee age greater than or equal to 30 but salary below 20000!");
        }
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> found = employeeRepository.findById(id);
        if (!found.get().getActive()) {
            throw new InactiveStatusException("Employee has been left with id: " + id);
        }
        if (updatedEmployee.getName() != null) found.get().setName(updatedEmployee.getName());
        if (updatedEmployee.getAge() != null) found.get().setAge(updatedEmployee.getAge());
        if (updatedEmployee.getGender() != null) found.get().setGender(updatedEmployee.getGender());
        if (updatedEmployee.getSalary() != null) found.get().setSalary(updatedEmployee.getSalary());
        return employeeMapper.toResponse(employeeRepository.save(found.get()));
    }

    public void deleteEmployee(int id) {
        Optional<Employee> deletedEmployee = employeeRepository.findById(id);

        deletedEmployee.get().setActive(false);
        employeeRepository.save(deletedEmployee.get());
    }
}
