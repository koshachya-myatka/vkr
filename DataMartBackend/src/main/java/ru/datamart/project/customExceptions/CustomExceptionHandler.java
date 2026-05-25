package ru.datamart.project.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.datamart.project.dto.other.ErrorResponseDto;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityExists(EntityAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(CustomInvalidRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleRequest(CustomInvalidRequestException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(CustomEntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(CustomEntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(ex.getMessage()));
    }
}