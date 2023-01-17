package com.example.oohq3ebeadando.Exceptions;

public class RollerScooterAlreadyExistsException extends Exception{
    public RollerScooterAlreadyExistsException() {
    }

    public RollerScooterAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollerScooterAlreadyExistsException(String message) {
        super(message);
    }
}
