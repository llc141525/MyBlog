package org.example.myblog.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserError implements ErrorCode {
    DUPLICATE_USER(1001, "重复的用户", HttpStatus.CONFLICT),
    INVALID_USER(1002, "用户名或者密码错误", HttpStatus.CONFLICT),
    INVALID_USERNAME(1003, "非法用户名", HttpStatus.CONFLICT),
    EMPTY_FILE(1004, "空文件异常", HttpStatus.BAD_REQUEST),
    NO_COOKIE(1005, "没有登陆", HttpStatus.UNAUTHORIZED),
    INVALID_FILE_TYPE(1006, "非法文件类型", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    USER_NOT_FOUND(1007, "用户不存在", HttpStatus.NOT_FOUND);


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

}
