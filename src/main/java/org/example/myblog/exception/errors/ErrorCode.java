package org.example.myblog.exception.errors;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    /**
     * 业务错误码
     */
    Integer getCode();

    /**
     * 面向用户的错误提示
     */
    String getMessage();

    /**
     * 对应的HTTP状态码
     */
    HttpStatus getHttpStatus();
}
