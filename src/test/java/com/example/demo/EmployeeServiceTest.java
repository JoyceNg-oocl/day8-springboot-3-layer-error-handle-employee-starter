package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void should_employee_when_create_an_employee() {
        Employee employee = new Employee(null, "Tom", 20, "MALE", 20000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);

        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employeeResult.getAge(), employee.getAge());
    }

    @Test
    void should_throw_exception_when_employee_of_greater_than_65_or_less_than_18() {
        Employee employee = new Employee(null, "Tom", 16, "MALE", 20000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);

        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_exception_when_employee_of_greater_than_or_equal_to_30_salary_below_20000() {
        Employee employee = new Employee(null, "Tom", 30, "MALE", 19999.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);

        assertThrows(InvalidSalaryEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_return_active_status_true_when_employee_created() {
        Employee employee = new Employee(null, "Tom", 20, "MALE", 20000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);

        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(true, employeeResult.getActiveStatus());
    }

}
