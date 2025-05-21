package org.example.myblog.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentError implements ErrorCode {
    ;
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
