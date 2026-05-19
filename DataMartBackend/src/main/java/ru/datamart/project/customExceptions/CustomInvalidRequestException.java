package ru.datamart.project.customExceptions;

public class CustomInvalidRequestException extends RuntimeException {
    public CustomInvalidRequestException(String message) {
        super(message);
    }
}