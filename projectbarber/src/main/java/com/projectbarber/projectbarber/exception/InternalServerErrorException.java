package com.projectbarber.projectbarber.exception;

// Tratativa de exceções de erro interno do servidor (500 Internal Server Error)
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
