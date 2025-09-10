package com.example.demo;

public class InvalidSalaryEmployeeException extends RuntimeException {
    public InvalidSalaryEmployeeException(String message) {
        super(message);
    }
}
