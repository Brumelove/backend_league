package com.protobuf.backend_league.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class MatrixExceptionHandler {

    @ExceptionHandler(InvalidMatrixException.class)
    public ResponseEntity<String> handleInvalidMatrixException(InvalidMatrixException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid matrix: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleMissingPart(MissingServletRequestPartException ex) {
        return ResponseEntity.badRequest().body("Missing required file part: " + ex.getRequestPartName());
    }

    @ExceptionHandler(CsvProcessingException.class)
    public ResponseEntity<String> handleCsvProcessing(CsvProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CSV Error: " + ex.getMessage());
    }
}
