package org.example.myblog.exception;

import lombok.Getter;
import lombok.Setter;
import org.example.myblog.exception.errors.ErrorCode;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
