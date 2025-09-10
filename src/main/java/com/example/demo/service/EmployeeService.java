package com.example.demo.service;

import com.example.demo.InactiveStatusException;
import com.example.demo.InvalidAgeEmployeeException;
import com.example.demo.InvalidSalaryEmployeeException;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return employeeRepository.getEmployees(gender, page, size);
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeRepository.getEmployeeById(id);
    }

    public Employee createEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidAgeEmployeeException("Employee age is null!");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("Employee age less than 18 or greater than 65!");
        }
        if (employee.getAge() >= 30 && employee.getSalary() < 20000) {
            throw new InvalidSalaryEmployeeException("Employee age greater than or equal to 30 but salary below 30000!");
        }
        return employeeRepository.createEmployee(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = getEmployeeById(id);
//        if (found == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
//        }
        if (!found.getActiveStatus()) {
            throw new InactiveStatusException("Employee has been left with id: " + id);
        }
        Employee employee = employeeRepository.updateEmployee(id, updatedEmployee);

        return employee;
    }

    public void deleteEmployee(int id) {
        Employee deletedEmployee = employeeRepository.getEmployeeById(id);
        if (deletedEmployee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        deletedEmployee.setActiveStatus(false);
        employeeRepository.updateEmployee(id, deletedEmployee);
    }

    public void empty() {
        employeeRepository.empty();
    }
}
