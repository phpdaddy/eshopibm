package com.phpdaddy.eshopibm.exception;

import org.springframework.http.HttpStatus;

public class WebApplicationException extends Exception {
    private HttpStatus status;

    public WebApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}