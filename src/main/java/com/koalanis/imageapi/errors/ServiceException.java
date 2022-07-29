package com.koalanis.imageapi.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
