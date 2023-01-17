package com.example.oohq3ebeadando.Exceptions;
public class BicycleAlreadyExistsException extends Exception{
    public BicycleAlreadyExistsException() {
    }

    public BicycleAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BicycleAlreadyExistsException(String message) {
        super(message);
    }
}
