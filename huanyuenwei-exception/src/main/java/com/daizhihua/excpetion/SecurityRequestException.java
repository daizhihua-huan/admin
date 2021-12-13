package com.daizhihua.excpetion;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class SecurityRequestException extends RuntimeException {
    private Integer status = BAD_REQUEST.value();

    public SecurityRequestException(String message) {
        super(message);
    }

    public SecurityRequestException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }
}
