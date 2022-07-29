package com.koalanis.imageapi.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Image Not Found")
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException() {
        super("Image not found");
    }
}
