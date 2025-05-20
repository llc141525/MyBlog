package org.example.myblog.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException{
    private int code;
    private String message;
    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
