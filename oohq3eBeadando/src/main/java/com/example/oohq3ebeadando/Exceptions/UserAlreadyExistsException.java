package com.example.oohq3ebeadando.Exceptions;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
