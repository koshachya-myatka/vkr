package ru.datamart.project.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.datamart.project.dto.ErrorResponseDto;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponseDto> handleEmail(InvalidEmailException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleRequest(InvalidRequestException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(ex.getMessage()));
    }
}