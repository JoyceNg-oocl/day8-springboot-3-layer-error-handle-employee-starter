package com.example.demo.service;

import com.example.demo.InactiveStatusException;
import com.example.demo.InvalidAgeEmployeeException;
import com.example.demo.InvalidSalaryEmployeeException;
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

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findAll(pageable).stream().toList();
            }
        } else {
            if (page == null || size == null) {
                return employeeRepository.findEmployeesByGender(gender);
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findEmployeesByGender(gender, pageable);
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee.get();
    }

    public Employee createEmployee(Employee employee) {
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
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = getEmployeeById(id);
        if (!found.getActiveStatus()) {
            throw new InactiveStatusException("Employee has been left with id: " + id);
        }
        if (updatedEmployee.getName() != null) found.setName(updatedEmployee.getName());
        if (updatedEmployee.getAge() != null) found.setAge(updatedEmployee.getAge());
        if (updatedEmployee.getGender() != null) found.setGender(updatedEmployee.getGender());
        if (updatedEmployee.getSalary() != null) found.setSalary(updatedEmployee.getSalary());
        return employeeRepository.save(found);
    }

    public void deleteEmployee(int id) {
        Employee deletedEmployee = getEmployeeById(id);

        deletedEmployee.setActiveStatus(false);
        employeeRepository.save(deletedEmployee);
    }
}
