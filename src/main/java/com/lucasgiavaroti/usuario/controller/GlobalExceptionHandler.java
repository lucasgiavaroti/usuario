package com.lucasgiavaroti.usuario.controller;

import com.lucasgiavaroti.usuario.infrastructure.exceptions.*;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.IllegalArgumentException;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "Not Found", request.getRequestURI()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException ex, HttpServletRequest request){
       return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrorResponse(HttpStatus.CONFLICT.value(),  ex.getMessage(), "Conflict", request.getRequestURI()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(HttpStatus.UNAUTHORIZED.value(),  ex.getMessage(), "Unauthorized", request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(HttpStatus.BAD_REQUEST.value(),  ex.getMessage(), "Bad Request", request.getRequestURI()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(BusinessException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),  ex.getMessage(), "Internal Server Error", request.getRequestURI()));
    }

    private ErrorResponseDTO buildErrorResponse(int status, String message, String error, String path){
        return new ErrorResponseDTO(LocalDateTime.now(), status, message, error, path);
    }

}
