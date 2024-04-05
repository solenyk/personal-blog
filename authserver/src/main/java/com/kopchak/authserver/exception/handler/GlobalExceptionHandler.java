package com.kopchak.authserver.exception.handler;

import com.kopchak.authserver.dto.error.ErrorInfoDto;
import com.kopchak.authserver.dto.error.MethodArgumentNotValidExceptionDto;
import com.kopchak.authserver.exception.exception.UsernameAlreadyExistException;
import com.kopchak.authserver.exception.exception.UsernameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ErrorInfoDto handleUsernameAlreadyExistException(HttpServletRequest req, UsernameAlreadyExistException ex) {
        return new ErrorInfoDto(req.getRequestURL().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorInfoDto handleUsernameNotFoundException(HttpServletRequest req, UsernameNotFoundException ex) {
        return new ErrorInfoDto(req.getRequestURL().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MethodArgumentNotValidExceptionDto handleValidationExceptions(HttpServletRequest req, MethodArgumentNotValidException e) {
        Map<String, String> fieldsErrorDetails = new LinkedHashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldsErrorDetails.put(fieldName, errorMessage);
        });
        return new MethodArgumentNotValidExceptionDto(req.getRequestURL().toString(), fieldsErrorDetails);
    }
}
