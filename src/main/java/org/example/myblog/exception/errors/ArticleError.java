package org.example.myblog.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ArticleError implements ErrorCode {
    ARTICLE_NOT_FOUND(2001, "未找到文章", HttpStatus.NOT_FOUND),
    PAGE_NOT_FOUND(2002, "页数错误, 必须是正整数", HttpStatus.NOT_FOUND),

    ;
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
