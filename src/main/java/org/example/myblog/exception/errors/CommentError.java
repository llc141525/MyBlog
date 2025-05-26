package org.example.myblog.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentError implements ErrorCode {
    COMMENT_NOT_FOUND(3001, "不存在的评论", HttpStatus.NOT_FOUND),
    ERROR_COMMENT_THREE(3002, "错误的评论树, 评论树最高两层", HttpStatus.BAD_REQUEST),
    ;
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
