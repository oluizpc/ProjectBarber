package com.projectbarber.projectbarber.exception;


// Tratativa de exceções em requisições inválidas (400 Bad Request)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
