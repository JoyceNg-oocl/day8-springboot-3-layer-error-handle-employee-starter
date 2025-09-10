package com.example.demo;

public class InvalidAgeEmployeeException extends RuntimeException {
    public InvalidAgeEmployeeException(String message) {
        super(message);
    }
}
