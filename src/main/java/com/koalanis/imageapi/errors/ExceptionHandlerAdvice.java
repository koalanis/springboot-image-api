package com.koalanis.imageapi.errors;

import com.koalanis.imageapi.models.errors.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value={BadRequestException.class})
    public ResponseEntity<Error> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(Error.builder().errorMessage(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={ServiceException.class})
    public ResponseEntity<Error> handleServiceException(ServiceException ex) {
        return new ResponseEntity<>(Error.builder().errorMessage(ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value={ImageNotFoundException.class})
    public ResponseEntity<Error> handleImageNotFoundException(ImageNotFoundException ex) {
        return new ResponseEntity<>(Error.builder().errorMessage(ex.getMessage()).build(), HttpStatus.NOT_FOUND);
    }
}
