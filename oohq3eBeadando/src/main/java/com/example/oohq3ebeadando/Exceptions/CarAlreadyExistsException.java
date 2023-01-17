package com.example.oohq3ebeadando.Exceptions;
public class CarAlreadyExistsException extends Exception{
    public CarAlreadyExistsException() {
    }

    public CarAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarAlreadyExistsException(String message) {
        super(message);
    }
}
