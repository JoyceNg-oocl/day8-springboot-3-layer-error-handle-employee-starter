package com.example.demo.controller.advice;

import com.example.demo.InactiveStatusException;
import com.example.demo.InvalidAgeEmployeeException;
import com.example.demo.InvalidSalaryEmployeeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException exceptionHandler(ResponseStatusException e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(InvalidAgeEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException exceptionHandler(InvalidAgeEmployeeException e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(InvalidSalaryEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException exceptionHandler(InvalidSalaryEmployeeException e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(InactiveStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException exceptionHandler(InactiveStatusException e) {
        return new ResponseException(e.getMessage());
    }
}
