package com.example.demo;

public class InactiveStatusException extends RuntimeException {
    public InactiveStatusException(String message) {
        super(message);
    }
}
