package com.team3.driveza.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedAppException extends RuntimeException {
    public AccessDeniedAppException() {
        super("Access denied");
    }
}