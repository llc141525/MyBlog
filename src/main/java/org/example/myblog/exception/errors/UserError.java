package org.example.myblog.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserError {
    DUPLICATE_USER(1001, "重复的用户"),
    INVALID_USER(1002, "用户名或者密码错误"),
    INVALID_USERNAME(1003, "非法用户名");


    private final Integer code;
    private final String message;

}
